using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace ScooterSharing
{
    // Learn more about making custom code visible in the Xamarin.Forms previewer
    // by visiting https://aka.ms/xamarinforms-previewer
    [DesignTimeVisible(false)]
    public partial class MainPage : ContentPage
    {
        public MainPage()
        {
            InitializeComponent();
        }

        async public void ToLogInOrAccount(object sender, EventArgs e)
        {
            scooter.FadeTo(1, 300, Easing.BounceOut);
            scooter.Play();
            await Task.Delay(2000);
            await util.FadeTo(0, 100, Easing.Linear);
            await scooter.FadeTo(0, 200, Easing.Linear);
            await circle.FadeTo(0, 200, Easing.Linear);
            await welcome.FadeTo(0, 200, Easing.Linear);
            await name.FadeTo(0, 300, Easing.Linear);

            object logged = "";
            if (!App.Current.Properties.TryGetValue("isLoggedIn", out logged))
            {
                App.Current.Properties.Add("isLoggedIn", "no");
            }

            if (App.Current.Properties.TryGetValue("isLoggedIn", out logged))
            {
                if(App.Current.Properties["isLoggedIn"].ToString() == "no")
                {
                    Application.Current.MainPage = new LogIn();
                }
                else
                {
                    Application.Current.MainPage = new LogIn();//Account();
                }
            }
            
            
        }
    }
}
