using System;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using System.Globalization;
using System.Resources;
using System.Reflection;
using System.Diagnostics;
using Plugin.Multilingual;

namespace ScooterSharing
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();
            MainPage = new MainPage();
        }

        /*async*/ protected override void OnStart()
        {
            // Handle when your app starts
           /* string result = await RequestStuff.doRequest("sas", "sas");
            if (result.Split('|')[1] == "no")
            {
                App.Current.Properties["res"] = "no";
                await App.Current.SavePropertiesAsync();
            }*/
        }

        protected override void OnSleep()
        {
            // Handle when your app sleeps
        }

        async protected override void OnResume()
        {
            // Handle when your app resumes
            if(App.Current.Properties["res"].ToString() != "no")
            {
                string result = await RequestStuff.doRequest("checkresstat", App.Current.Properties["resId"].ToString());
                if (result.Split('|')[0] != RequestResult.OK.ToString())
                    return;
                if (result.Split('|')[1] == "false")
                {
                    App.Current.Properties["res"] = "no";
                    await App.Current.SavePropertiesAsync();
                }
            }
            
        }
    }
}
