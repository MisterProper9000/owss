using System;
using System.Collections.Generic;
using System.Globalization;
using ScooterSharing.Extensions;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace ScooterSharing
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AccountDetail : ContentPage
    {
        private Dictionary<string, string> langs = new Dictionary<string, string>
        {
            { "English", "en" },
            { "Русский", "ru" },
            { "Deutsch", "de" },
            { "Español", "es" },
            { "Tiếng việt nam", "vi" },
        };

        public AccountDetail()
        {
            
            InitializeComponent();
            
            logout.Source = ImageSource.FromResource("ScooterSharing.logout.png");

            foreach (string lang in langs.Keys)
            {
                langSelect.Items.Add(lang);
            }
            First_Name.Text = App.Current.Properties["fName"].ToString();
            Last_Name.Text = App.Current.Properties["lName"].ToString();
            
        }
        async private void logOut(object sender, EventArgs e)
        {
            if(App.Current.Properties["rent"].ToString() == "yes")
            {
                await DisplayAlert(AppRes.Attention, AppRes.You_can_t_log_out_during_renting, AppRes.OK);
                return;
            }
            string action = await DisplayActionSheet(AppRes.Exit_are_you_sure, AppRes.Cancel, AppRes.OK);
            if (action == AppRes.OK)
            {
                App.Current.Properties["isLoggedIn"] = "no";
                await App.Current.SavePropertiesAsync();
                App.Current.MainPage = new LogIn();
            }
        }

        async private void setLang(object sender, EventArgs e)
        {
            var name = langSelect.Items[langSelect.SelectedIndex];
            AppRes.Culture = new CultureInfo(langs[name]);
            Translator.Instance.Invalidate();
            App.Current.Properties["lang"] = langs[name];
            await App.Current.SavePropertiesAsync();
        }
    }
}