using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using ScooterSharing;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

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
            //Console.WriteLine("ssssssssssssss      //" + result);
            cash.Text = result;
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
            await Navigation.PushAsync(new Offers(), true);
        }
        async private void ToPay(object sender, EventArgs e)
        {
            TabbedPage tabbedPage = new TabbedPage();
            tabbedPage.Children.Add(new Pay());
            tabbedPage.Children.Add(new UnPay());
            tabbedPage.Title = AppRes.Payments;
            tabbedPage.BarBackgroundColor = Color.Green;
            await Navigation.PushAsync(tabbedPage, true);
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

        protected override void OnAppearing()
        {
            SetBalance();
            base.OnAppearing();
        }
    }
}