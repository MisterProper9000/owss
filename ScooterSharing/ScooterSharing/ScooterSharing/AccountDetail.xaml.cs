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
    public partial class AccountDetail : ContentPage
    {
        public AccountDetail()
        {
            InitializeComponent();

            logout.Source = ImageSource.FromResource("ScooterSharing.logout.png");
        }
        async private void logOut(object sender, EventArgs e)
        {
            App.Current.Properties["isLoggedin"] = "no";
            await App.Current.SavePropertiesAsync();
            App.Current.MainPage = new LogIn();
        }
    }
}