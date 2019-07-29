using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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

        }

        private void PayTapped(object sender, ItemTappedEventArgs e)
        {
            var vm = BindingContext as PaymentsViewModel;
            var payment = e.Item as Payment;
            vm.HideOrShowPayment(payment);
        }
    }
}