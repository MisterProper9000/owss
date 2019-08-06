using Newtonsoft.Json;
using System;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace ScooterSharing
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class Rent : ContentPage
    {
        public DateTime startRent;
        bool finishRent = false;
        bool block = false;//предполагалось блокировать юзера на странице, если он завершил оплату, но чёт пока это нет и вроде не надо будет
        bool toOffers = false;
        public Rent()
        {
            InitializeComponent();
            tariff.Text = App.Current.Properties["showtariff"].ToString();
            if (App.Current.Properties["rent"].ToString() == "yes" && App.Current.Properties["block"].ToString() != "true")
            {
                startRent = Convert.ToDateTime(App.Current.Properties["startRent"].ToString());
                Device.StartTimer(TimeSpan.FromSeconds(1), () =>
                {
                    // Do something
                    if (finishRent)
                    {
                        return false;
                    }

                    DateTime now = DateTime.Now;
                    start.Text = (int)now.Subtract(startRent).TotalHours + ":" + (int)now.Subtract(startRent).TotalMinutes%60 + ":" + (int)now.Subtract(startRent).TotalSeconds%60;

                    return true; // True = Repeat again, False = Stop the timer
                });
                start.IsEnabled = true;
                start.IsVisible = true;
                stop.IsVisible = true;
                stop.IsEnabled = true;
                btnstart.IsVisible = false;
                btnstart.IsEnabled = false;
                ToOffers.IsEnabled = false;
                ToOffers.IsVisible = false;

            }

            if (App.Current.Properties["block"].ToString() == "true")
            {
                start.Text = App.Current.Properties["time"].ToString();
                cost.Text = AppRes.Rental_cost + ": " + App.Current.Properties["cost"].ToString() + App.Current.Properties["balance"].ToString().Split(' ')[1];
                start.IsEnabled = true;
                start.IsVisible = true;
                stop.IsVisible = false;
                stop.IsEnabled = false;
                btnstart.IsVisible = false;
                btnstart.IsEnabled = false;
                cost.IsEnabled = true;
                cost.IsVisible = true;
                pay.IsVisible = true;
                pay.IsEnabled = true;
                ToOffers.IsEnabled = false;
                ToOffers.IsVisible = false;

            }
        }

        async private void Pay(object sender, EventArgs e)
        {
            ToOffers.IsEnabled = false;
            ToOffers.IsVisible = false;

            string result = await RequestStuff.doRequest("payRent", App.Current.Properties["id"].ToString()); //пока не хендлим ошибки тут
            string[] parseRes = result.Split('|');
            if (parseRes[0] != RequestResult.OK.ToString())
            {
                await DisplayAlert(AppRes.Attention, AppRes.Something_wrong_with_server, AppRes.OK);
                return;
            }
            App.Current.Properties["rent"] = "no";
            App.Current.Properties["startRent"] = "";
            App.Current.Properties["block"] = "false";
            block = false;
            await App.Current.SavePropertiesAsync();
            await Navigation.PopAsync(true);
        }

        async private void goToOffers(object sender, EventArgs e)
        {
            toOffers = true;
            await Navigation.PushAsync(new Offers(),true);
        }


        async private void Stop(object sender, EventArgs e)
        {
            block = true;
            string result = await RequestStuff.doRequest("ardend", App.Current.Properties["id"].ToString()); //пока не хендлим ошибки тут
            string[] parseRes = result.Split('|');
            if(parseRes[0]!=RequestResult.OK.ToString())
            {
                await DisplayAlert(AppRes.Attention, AppRes.Something_wrong_with_server, AppRes.OK);
                return;
            }
            App.Current.Properties["block"] = "true";
            App.Current.Properties["cost"] = parseRes[1];
            App.Current.Properties["time"] = start.Text;
            await App.Current.SavePropertiesAsync();
            stop.IsEnabled = false;
            stop.IsVisible = false;
            pay.IsVisible = true;
            pay.IsEnabled = true;
            cost.IsEnabled = true;
            cost.IsVisible = true;
            cost.Text += ": " + parseRes[1].ToString() + App.Current.Properties["balance"].ToString().Split(' ')[1];
            
            finishRent = true;
        }

        async private void StartRent(object sender, EventArgs e)
        {
            var Qr = new QRstart
            {
                period = App.Current.Properties["tariff"].ToString().Split('|')[1],
                tariff = App.Current.Properties["tariff"].ToString().Split('|')[0],
                qr = App.Current.Properties["qr"].ToString(),
                email = App.Current.Properties["email"].ToString()
            };

            string result = await RequestStuff.doRequest("checkresstat", App.Current.Properties["resId"].ToString());
            if (result.Split('|')[0] != RequestResult.OK.ToString())
                return;
            if (result.Split('|')[1] == "false" && App.Current.Properties["res"].ToString() != "no")
            {
                App.Current.Properties["res"] = "no";
                await App.Current.SavePropertiesAsync();
                await DisplayAlert("Attention", "You reservation was expired", "OK");
            }

            result = await RequestStuff.doRequest("ardstart", JsonConvert.SerializeObject(Qr));
            string[] parseResult = result.Split('|');
            Console.WriteLine("GOVNO "+result);
            if (parseResult[0] == RequestResult.OK.ToString())
            {
                App.Current.Properties["res"] = "no";
                App.Current.Properties["id"] = parseResult[1];
                await App.Current.SavePropertiesAsync();
                btnstart.IsEnabled = false;
                start.IsEnabled = true;
                stop.IsEnabled = true;
                if (App.Current.Properties["startRent"].ToString() != "")
                    startRent = Convert.ToDateTime(App.Current.Properties["startRent"].ToString());
                else
                    startRent = DateTime.Now;
                App.Current.Properties["rent"] = "yes";
                App.Current.Properties["startRent"] = startRent.ToString();
                await App.Current.SavePropertiesAsync();
                start.IsEnabled = true;
                start.IsVisible = true;
                stop.IsVisible = true;
                stop.IsEnabled = true;
                btnstart.IsVisible = false;
                btnstart.IsEnabled = false;
                ToOffers.IsEnabled = false;
                ToOffers.IsVisible = false;
                Device.StartTimer(TimeSpan.FromSeconds(1), () =>
                {
                    // Do something
                    if (finishRent)
                    {
                        return false;
                    }

                    DateTime now = DateTime.Now;
                    start.Text = (int)now.Subtract(startRent).TotalHours + ":" + (int)now.Subtract(startRent).TotalMinutes + ":" + (int)now.Subtract(startRent).TotalSeconds;

                    return true; // True = Repeat again, False = Stop the timer
                });
            }
            else if (parseResult[0] == RequestResult.DOESNTEXIST.ToString())
            {
                await DisplayAlert(AppRes.Error, "Wrong Qr", AppRes.OK);
            }
            else if (parseResult[0] == RequestResult.RENTALREADYSTARTED.ToString())
            {
                await DisplayAlert(AppRes.Error, AppRes.This_scooter_already_rented_or_reserved, AppRes.OK);
            }
            else if (parseResult[0] == RequestResult.NOTENOUGH.ToString())
            {
                await DisplayAlert(AppRes.Attention, AppRes.Not_enough_money_for_deposit, AppRes.OK);
            }
            else if (parseResult[0] == RequestResult.BLOCKED.ToString())
            {
                await DisplayAlert(AppRes.Attention, AppRes.This_scooter_already_rented_or_reserved, AppRes.OK);
                return;
            }
            else
            {
                await DisplayAlert(AppRes.Error, AppRes.Something_goes_wrong, AppRes.OK);
            }
        }
        protected override bool OnBackButtonPressed()
        {
            if (block)
                return false;
            return base.OnBackButtonPressed();
        }
        protected override void OnDisappearing()
        {
            if(!toOffers)
                ((Account)((TabbedPage)Navigation.NavigationStack[Navigation.NavigationStack.Count - 2]).Children[0]).SetQRButtons();
            
            //base.OnDisappearing();
            //this.Content = null;
        }

        protected override void OnAppearing()
        {
            tariff.Text = App.Current.Properties["showtariff"].ToString();
            //base.OnAppearing();
        }
    }
}