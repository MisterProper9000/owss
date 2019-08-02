using ScooterSharing.Extensions;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
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
            //data to save in app current property: first name, last name, balance
            if (!App.Current.Properties.ContainsKey("fName"))
            {
                App.Current.Properties.Add("fName", "");
            }
            if (!App.Current.Properties.ContainsKey("lName"))
            {
                App.Current.Properties.Add("lName", "");
            }
            if (!App.Current.Properties.ContainsKey("balance"))
            {
                App.Current.Properties.Add("balance", "");
            }
            if (!App.Current.Properties.ContainsKey("lang"))
            {
                App.Current.Properties.Add("lang", "");
            }
            if (!App.Current.Properties.ContainsKey("email"))
            {
                App.Current.Properties.Add("email", "");
            }
            if (!App.Current.Properties.ContainsKey("rent"))
            {
                App.Current.Properties.Add("rent", "no");
            }
            if (!App.Current.Properties.ContainsKey("block"))
            {
                App.Current.Properties.Add("block", "no");
            }
            if (!App.Current.Properties.ContainsKey("tariff"))
            {
                App.Current.Properties.Add("tariff", "1|1");
            }
            if (!App.Current.Properties.ContainsKey("startRent"))
            {
                App.Current.Properties.Add("startRent", "");
            }
            if (!App.Current.Properties.ContainsKey("qr"))
            {
                App.Current.Properties.Add("qr", "");
            }
            if (!App.Current.Properties.ContainsKey("id"))
            {
                App.Current.Properties.Add("id", "");
            }
            if (!App.Current.Properties.ContainsKey("time"))
            {
                App.Current.Properties.Add("time", "");
            }
            if (!App.Current.Properties.ContainsKey("cost"))
            {
                App.Current.Properties.Add("cost", "");
            }
            if (!App.Current.Properties.ContainsKey("showtariff"))
            {
                App.Current.Properties.Add("showtariff", "");
            }
            if (!App.Current.Properties.ContainsKey("res"))//запрещаем юзеру бронить больше одного скутера
            {
                App.Current.Properties.Add("res", "no");
            }
            if (!App.Current.Properties.ContainsKey("resId"))//id заказа на бронь
            {
                App.Current.Properties.Add("resId", "");
            }
            if (App.Current.Properties["lang"].ToString() != "")
            {
                AppRes.Culture = new CultureInfo(App.Current.Properties["lang"].ToString());
                Translator.Instance.Invalidate();
            }
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
                await App.Current.SavePropertiesAsync();
            }

            if (App.Current.Properties.TryGetValue("isLoggedIn", out logged))
            {
                Console.WriteLine(App.Current.Properties["isLoggedIn"].ToString());
                if(App.Current.Properties["isLoggedIn"].ToString() == "no")
                {
                    Application.Current.MainPage = new LogIn();
                }
                else
                {
                    if (App.Current.Properties["rent"].ToString() == "yes")
                    {
                        TabbedPage tabbedPage = new TabbedPage();
                        tabbedPage.Children.Add(new Account());
                        tabbedPage.Children.Add(new XMap());

                        tabbedPage.BarBackgroundColor = Color.Green;
                        NavigationPage.SetHasNavigationBar(tabbedPage, false);
                        Application.Current.MainPage = new NavigationPage(tabbedPage);
                    }
                    else
                    {
                        TabbedPage tabbedPage = new TabbedPage();
                        tabbedPage.Children.Add(new Account());
                        tabbedPage.Children.Add(new XMap());

                        tabbedPage.BarBackgroundColor = Color.Green;
                        NavigationPage.SetHasNavigationBar(tabbedPage, false);
                        Application.Current.MainPage = new NavigationPage(tabbedPage);
                    }
                }
            }
        }

      
    }
}
