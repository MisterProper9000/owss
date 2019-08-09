using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using ScooterSharing;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Xamarin.Forms.PlatformConfiguration;

namespace ScooterSharing
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class Account : ContentPage
    {
        public Account()
        {
            InitializeComponent();
            backPhoto.Source = ImageSource.FromResource("ScooterSharing.back.jpg");
            offers.Source = ImageSource.FromResource("ScooterSharing.candy.png");
            userIcon.Source = ImageSource.FromResource("ScooterSharing.user.png");
            wallet.Source = ImageSource.FromResource("ScooterSharing.wallet.png");
            if(App.Current.Properties["rent"].ToString() == "no")
            {
                QR.IsEnabled = true;
                toRentDetails.IsEnabled = false;
            }
            else
            {
                QR.IsEnabled = false;
                toRentDetails.IsEnabled = true;
            }
            if (App.Current.Properties["rent"].ToString() == "yes")
            {
                _ToRentDetails();
            }
            
        }
        async private void SetBalance()
        {
            string result = await RequestStuff.doRequest("balanceInquery", App.Current.Properties["email"].ToString());
            App.Current.Properties["balance"] = result;
            await App.Current.SavePropertiesAsync();
            //Console.WriteLine("ssssssssssssss      //" + result);
            cash.Text = result.Replace("USD","$");
        }
        public void SetQRButtons()
        {
            if (App.Current.Properties["rent"].ToString() == "no")
            {
                QR.IsEnabled = true;
                toRentDetails.IsEnabled = false;
            }
            else
            {
                QR.IsEnabled = false;
                toRentDetails.IsEnabled = true;
            }
        }

        private void PayTapped(object sender, ItemTappedEventArgs e)
        {
            var vm = BindingContext as PaymentsViewModel;
            
            var payment = e.Item as Payment;
            vm.HideOrShowPayment(payment);
        }

        async private void toDetails(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new AccountDetail(), true);
        }

        async private void ToOffers(object sender, EventArgs e)
        {
            notifications.IsVisible = false;
            notifications.IsEnabled = false;
            notificationsTxt.Text = "0";
            App.Current.Properties["notifications"] = "0";
            await App.Current.SavePropertiesAsync();
            await Navigation.PushAsync(new Offers(), true);
        }
        async private void ToPay(object sender, EventArgs e)
        {
            TabbedPage tabbedPage = new TabbedPage();
            tabbedPage.Children.Add(new Pay());
            tabbedPage.Children.Add(new UnPay());
            
            //tabbedPage.UnselectedTabColor = Color.Black;
            //tabbedPage.SelectedTabColor = Color.Red;
            tabbedPage.BarBackgroundColor = Color.DarkSeaGreen;
            tabbedPage.Title = AppRes.Payments;
            
            //tabbedPage.BarTextColor = Color.DarkGreen;
            await Navigation.PushAsync(tabbedPage, true);
            ((NavigationPage)Application.Current.MainPage).BarBackgroundColor = Color.Green;
        }
        async private void _ToRentDetails()
        {
            await Navigation.PushAsync(new Rent(), true);
        }

        async private void ToRentDetails(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new Rent(), true);            
        }

        async private void scanQr(object sender, EventArgs e)
        {
            /*Console.WriteLine("ssssssssssssss   "+double.Parse(App.Current.Properties["balance"].ToString().Split('.')[0]));//это чёт не работало нафиг на устройстве
            if (
            {
                await DisplayAlert(AppRes.Attention, AppRes.Not_enough_money_for_deposit, AppRes.OK);
                return;
            }*/
            try
            {
                var scanner = DependencyService.Get<IQrScannerService>();

                var qr = await scanner.ScanAsync();
                qr = "sfb_moto:1";//emulator back cam doesn't connect to webcam :(
                if (qr != null)
                {   
                    App.Current.Properties["qr"] = qr;
                    await App.Current.SavePropertiesAsync();
                    await Navigation.PushAsync(new Rent(), true);                    
                }
                
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        async protected override void OnAppearing()
        {
            var vm = BindingContext as PaymentsViewModel;
            vm.Update();

            if (App.Current.Properties["res"].ToString() != "no")
            {
                string result = await RequestStuff.doRequest("checkresstat", App.Current.Properties["resId"].ToString());
                
                if (result.Split('|')[0] != RequestResult.OK.ToString())
                    return;
                if (result.Split('|')[1] == "false" && App.Current.Properties["res"].ToString() != "no")
                {
                    App.Current.Properties["res"] = "no";
                    await App.Current.SavePropertiesAsync();
                    await DisplayAlert("Attention", "You reservation was expired", "OK");
                    Console.WriteLine("PEREUSMAMS " + result + " " + App.Current.Properties["res"].ToString());
                }
            }

            if (App.Current.Properties["notifications"].ToString() != "0")
            {
                notifications.IsVisible = true;
                notifications.IsEnabled = true;
                notificationsTxt.Text = App.Current.Properties["notifications"].ToString();
            }
            else
            {
                notifications.IsVisible = false;
                notifications.IsEnabled = false;
            }

            if (App.Current.Properties["rent"].ToString() == "no")
            {
                QR.IsEnabled = true;
                toRentDetails.IsEnabled = false;
            }
            else
            {
                QR.IsEnabled = false;
                toRentDetails.IsEnabled = true;
            }

            SetBalance();
            base.OnAppearing();
        }
    }
}