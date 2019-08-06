using System;
using System.Collections.Generic;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Newtonsoft.Json;
using System.Text.RegularExpressions;
using System.Globalization;
using ScooterSharing.Extensions;

namespace ScooterSharing
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    
    public partial class LogIn : ContentPage
    {
        private bool mailOK = false;
        private bool phoneOK = false;
        private int fsNameCharLimit = 40;
        private int phoneCharLimit = 17;
        private enum UIMode{Default,LogIn,SignUp };
        int onFinishCheck; // since both click and close button's animation have to be played (for draw on last frame)
        int onFinishClose; // during moving to input mode both onFinish methods call before they actually will
                           // need, there is a need in counter of OnFinish calls for both OnFinish methods

        private Dictionary<string, string> langs = new Dictionary<string, string>
        {
            { "English", "en" },
            { "Русский", "ru" },
            { "Deutsch", "de" },
            { "Español", "es" },
            { "Tiếng việt nam", "vi" },
        };


        UIMode uiMode;
        public LogIn()
        {
            InitializeComponent();
            uiMode = UIMode.Default;
            onFinishClose = 0;
            onFinishCheck = 0;

            foreach (string lang in langs.Keys)
            {
                changeLan.Items.Add(lang);
            }
        }
        async public void changeLang(object sender, EventArgs e)
        {
            var name = changeLan.Items[changeLan.SelectedIndex];
            AppRes.Culture = new CultureInfo(langs[name]);
            Translator.Instance.Invalidate();
            App.Current.Properties["lang"] = langs[name];
            await App.Current.SavePropertiesAsync();
        }
        public void MoveToSignUp(object sender, EventArgs e)
        {
            uiMode = UIMode.SignUp;

            changeLan.IsEnabled = false;
            changeLan.IsVisible = false;

            check.Opacity = 1;
            check.Play();
            check.Opacity = 0;
            close.Opacity = 1;
            close.Play();
            close.Opacity = 0;

            btnSignUp.TranslateTo(0, btnSignUp.Y + btnSignUp.Height/2, 600, Easing.BounceOut);
            btnSignUp.ScaleTo(0.2, 650, Easing.SinOut);
            btnSignUp.FadeTo(0, 650, Easing.Linear);

            btnSignUp.IsEnabled = false;
            btnLogIn.IsEnabled = false;
            

            fName.IsEnabled = true;
            fName.IsVisible = true;
            sName.IsEnabled = true;
            sName.IsVisible = true;
            phone.IsEnabled = true;
            phone.IsVisible = true;
            mail.IsEnabled = true;
            mail.IsVisible = true;
            password.IsEnabled = true;
            password.IsVisible = true;
            confirmPassword.IsEnabled = true;
            confirmPassword.IsVisible = true;

            btnLogIn.FadeTo(0, 100, Easing.Linear);
            fName.FadeTo(1, 100, Easing.Linear);
            sName.FadeTo(1, 200, Easing.Linear);
            phone.FadeTo(1, 300, Easing.Linear);
            mail.FadeTo(1, 400, Easing.Linear);
            password.FadeTo(1, 500, Easing.Linear);
            
            confirmPassword.FadeTo(1, 600, Easing.Linear);
            check.FadeTo(1, 600, Easing.Linear);
            close.FadeTo(1, 600, Easing.Linear);
            check.IsEnabled = true;
            close.IsEnabled = true;
        }

        public void MoveToLogIn(object sender, EventArgs e)
        {
            uiMode = UIMode.LogIn;

            changeLan.IsEnabled = false;
            changeLan.IsVisible = false;

            check.Opacity = 1;
            check.Play();
            check.Opacity = 0;
            close.Opacity = 1;
            close.Play();
            close.Opacity = 0;

            btnLogIn.TranslateTo(0, btnSignUp.Y + btnSignUp.Height / 2, 600, Easing.BounceOut);
            btnLogIn.ScaleTo(0.2, 650, Easing.SinOut);
            btnLogIn.FadeTo(0, 650, Easing.Linear);

            btnSignUp.IsEnabled = false;
            btnLogIn.IsEnabled = false;

            mail.IsEnabled = true;
            mail.IsVisible = true;
            password.IsEnabled = true;
            password.IsVisible = true;

            btnSignUp.FadeTo(0, 100, Easing.Linear);

            mail.FadeTo(1, 400, Easing.Linear);
            password.FadeTo(1, 500, Easing.Linear);

            check.FadeTo(1, 600, Easing.Linear);
            close.FadeTo(1, 600, Easing.Linear);
            check.IsEnabled = true;
            close.IsEnabled = true;
        }

        public void DataCheck(object sender, EventArgs e)
        {
            check.Play();
            
            if(uiMode == UIMode.SignUp && (fName.Text == "" || sName.Text == "" || phone.Text == "" || mail.Text == "" ||
                password.Text == "" || confirmPassword.Text == ""))
            {
                DisplayAlert(AppRes.Attention, AppRes.All_fields_must_be_filled, AppRes.OK);
                return;
            }

            //if (uiMode == UIMode.SignUp && phone.Text.Length < phoneCharLimit)
            //{
            //    DisplayAlert(AppRes.Attention, AppRes.Too_short_phone_number, AppRes.OK);
            //    return;
            //}

            if(!phoneOK)
            {
                DisplayAlert(AppRes.Attention, AppRes.Too_short_phone_number, AppRes.OK);
                return;
            }

            if(!mailOK)
            {
                DisplayAlert(AppRes.Attention, AppRes.Invalid_email, AppRes.OK);
                return;
            }

            if (uiMode == UIMode.LogIn && (mail.Text == "" || password.Text == ""))
            {
                DisplayAlert(AppRes.Attention, AppRes.All_fields_must_be_filled, AppRes.OK);
                return;
            }

            if (uiMode == UIMode.SignUp && confirmPassword.Text != password.Text)
            {
                DisplayAlert(AppRes.Attention, AppRes.Mismatched_passwords, AppRes.OK);
                return;
            }
            if (uiMode == UIMode.SignUp)
            {
                DisplayAlert(AppRes.Attention, AppRes.A_confirmation_code_will_be_sent_to_your_email, AppRes.OK);

                fName.IsEnabled = false;
                fName.IsVisible = false;
                sName.IsEnabled = false;
                sName.IsVisible = false;
                phone.IsEnabled = false;
                phone.IsVisible = false;
                mail.IsEnabled = false;
                mail.IsVisible = false;
                password.IsEnabled = false;
                password.IsVisible = false;
                confirmPassword.IsEnabled = false;
                confirmPassword.IsVisible = false;

                close.IsEnabled = false;
                close.Opacity = 0;
                check.Opacity = 0;
                check.IsEnabled = false;

                code.IsEnabled = true;
                code.IsVisible = true;
                sendCode.IsVisible = true;
                sendCode.IsEnabled = true;
            }

            if(uiMode == UIMode.LogIn)
            {
                ToAccountFromLogIn();
            }
        }

        public void ToStart(object sender, EventArgs e)
        {         
            if(onFinishClose == 0)
            {
                onFinishClose++;
                return;
            }
            onFinishCheck = 0;
            onFinishClose = 0;
            if (uiMode == UIMode.LogIn)
            {
                ToStartFromLogin();
            }
            else if(uiMode == UIMode.SignUp)
            {
                ToStartFromSignUp();
            }
            uiMode = UIMode.Default;
        }

        public void ToStartFromSignUp()
        {
            btnSignUp.TranslateTo(0, 0, 600, Easing.BounceOut);
            btnSignUp.ScaleTo(1, 650, Easing.SinOut);
            btnSignUp.FadeTo(1, 650, Easing.Linear);

            btnSignUp.IsEnabled = true;
            btnLogIn.IsEnabled = true;

            btnLogIn.FadeTo(1, 100, Easing.Linear);
            fName.FadeTo(0, 100, Easing.Linear);
            sName.FadeTo(0, 200, Easing.Linear);
            phone.FadeTo(0, 300, Easing.Linear);
            mail.FadeTo(0, 400, Easing.Linear);
            password.FadeTo(0, 500, Easing.Linear);

            confirmPassword.FadeTo(0, 600, Easing.Linear);
            check.FadeTo(0, 600, Easing.Linear);
            close.FadeTo(0, 600, Easing.Linear);

            sendCode.IsEnabled = false;
            sendCode.IsVisible = false;
            fName.IsEnabled = false;
            fName.IsVisible = false;
            sName.IsEnabled = false;
            sName.IsVisible = false;
            phone.IsEnabled = false;
            phone.IsVisible = false;
            mail.IsEnabled = false;
            mail.IsVisible = false;
            password.IsEnabled = false;
            password.IsVisible = false;
            confirmPassword.IsEnabled = false;
            confirmPassword.IsVisible = false;
            check.IsEnabled = false;
            close.IsEnabled = false;

            changeLan.IsEnabled = true;
            changeLan.IsVisible = true;
        }
        public void ToStartFromLogin()
        {
            btnLogIn.TranslateTo(0, 0, 600, Easing.BounceOut);
            btnLogIn.ScaleTo(1, 650, Easing.SinOut);
            btnLogIn.FadeTo(1, 650, Easing.Linear);

            btnSignUp.IsEnabled = true;
            btnLogIn.IsEnabled = true;
            
            sendCode.IsEnabled = false;
            sendCode.IsVisible = false;
            mail.IsEnabled = false;
            mail.IsVisible = false;
            password.IsEnabled = false;
            password.IsVisible = false;
            check.IsEnabled = false;
            close.IsEnabled = false;

            changeLan.IsEnabled = true;
            changeLan.IsVisible = true;

            btnSignUp.FadeTo(1, 100, Easing.Linear);
            mail.FadeTo(0, 400, Easing.Linear);
            password.FadeTo(0, 500, Easing.Linear);

            check.FadeTo(0, 600, Easing.Linear);
            close.FadeTo(0, 600, Easing.Linear);
        }

        public void PlayClose(object sender, EventArgs e)
        {
            close.Play();
        }

        async private void ToAccountFromLogIn()
        {
            //tmp
            /*TabbedPage tabbedPage = new TabbedPage();
            tabbedPage.Children.Add(new Account());
            tabbedPage.Children.Add(new XMap());

            tabbedPage.BarBackgroundColor = Color.Green;
            NavigationPage.SetHasNavigationBar(tabbedPage, false);
            Application.Current.MainPage = new NavigationPage(tabbedPage);
            //tmp
            return;*/
            var user = new UserLogIn
            {
                password = password.Text,
                email = mail.Text
            };
            string result = await RequestStuff.doRequest("loginclient", JsonConvert.SerializeObject(user));
            string[] parseRes = result.Split('|');
            if (parseRes[0] == RequestResult.OK.ToString())
            {
                App.Current.Properties["isLoggedIn"] = "yes";
                App.Current.Properties["fName"] = parseRes[1];
                App.Current.Properties["lName"] = parseRes[2];
                App.Current.Properties["email"] = mail.Text;
                //App.Current.Properties["balance"] = parseResult.balance;
                await App.Current.SavePropertiesAsync();

                TabbedPage tabbedPage = new TabbedPage();
                tabbedPage.Children.Add(new Account());
                tabbedPage.Children.Add(new XMap());

                tabbedPage.BarBackgroundColor = Color.Green;
                NavigationPage.SetHasNavigationBar(tabbedPage, false);
                Application.Current.MainPage = new NavigationPage(tabbedPage);
            }
            else if(parseRes[0] == RequestResult.DOESNTEXIST.ToString())
            {
                await DisplayAlert(AppRes.Error, AppRes.User_doesn_t_exist, AppRes.OK);
                if (uiMode == UIMode.LogIn)
                {
                    ToStartFromLogin();
                    onFinishClose = 0;
                }
                if (uiMode == UIMode.SignUp)
                {
                    ToStartFromSignUp();
                    onFinishClose = 0;
                }
            }
            else
            {
                await DisplayAlert(AppRes.Error, AppRes.Something_goes_wrong, AppRes.OK);
                if (uiMode == UIMode.LogIn)
                {
                    ToStartFromLogin();
                    onFinishClose = 0;
                }
                if (uiMode == UIMode.SignUp)
                {
                    ToStartFromSignUp();
                    onFinishClose = 0;
                }
            }
            
        }

        async public void ToAccount()
        {
            var user = new UserSignUp {
                first_name = fName.Text,
                last_name = sName.Text,
                phone = phone.Text,
                email = mail.Text,
                password = password.Text
            };
            string result = await RequestStuff.doRequest("regclient", JsonConvert.SerializeObject(user));
            string[] parseRes = result.Split('|');
            if (parseRes[0] == RequestResult.ALREADYEXIST.ToString())
            {
                await DisplayAlert(AppRes.Error, AppRes.User_already_exists, AppRes.OK);
                if (uiMode == UIMode.LogIn)
                {
                    ToStartFromLogin();
                    onFinishClose = 0;
                }
                if (uiMode == UIMode.SignUp)
                {
                    ToStartFromSignUp();
                    onFinishClose = 0;
                }
                return;
            }
            if (parseRes[0] != RequestResult.OK.ToString())
            {
                await DisplayAlert(AppRes.Error, AppRes.Something_wrong_with_server, AppRes.OK);
                if (uiMode == UIMode.LogIn)
                {
                    ToStartFromLogin();
                    onFinishClose = 0;
                }
                if (uiMode == UIMode.SignUp)
                {
                    ToStartFromSignUp();
                    onFinishClose = 0;
                }
                return;
            }
            App.Current.Properties["isLoggedIn"] = "yes";
            App.Current.Properties["fName"] = fName.Text;
            App.Current.Properties["lName"] = sName.Text;
            App.Current.Properties["email"] = mail.Text;
            App.Current.Properties["balance"] = "0$";
            await App.Current.SavePropertiesAsync();
            TabbedPage tabbedPage = new TabbedPage();
            tabbedPage.Children.Add(new Account());
            tabbedPage.Children.Add(new XMap());

            tabbedPage.BarBackgroundColor = Color.Green;
            NavigationPage.SetHasNavigationBar(tabbedPage, false);
            Application.Current.MainPage = new NavigationPage(tabbedPage);
        }

        protected override bool OnBackButtonPressed()
        {

            return true;
        }

        // Check if data is correct during unput (or when field is unfocused in case of password or mail fields)
        public void DetectTrash(object sender, EventArgs e)
        {
            var entry = (Entry) sender;
            switch(entry.ClassId)
            {
                case "fName":
                    if (fName.Text.Length > 0)
                        if (!Char.IsLetter(fName.Text[fName.Text.Length - 1]))
                        {
                            fName.Text = fName.Text.Substring(0, fName.Text.Length - 1);
                        }
                    if (fName.Text.Length > fsNameCharLimit)
                    {
                        fName.Text = fName.Text.Substring(0, fsNameCharLimit);
                    }
                    break;
                case "sName":
                    if (sName.Text.Length > 0)
                        if (!Char.IsLetter(sName.Text[sName.Text.Length - 1]))
                        {
                            sName.Text = sName.Text.Substring(0, sName.Text.Length - 1);
                        }
                    if (sName.Text.Length > fsNameCharLimit)
                    {
                        sName.Text = sName.Text.Substring(0, fsNameCharLimit);
                    }
                    break;
                case "phone":
                    if (phone.Text.Length > 0)
                        if (!Char.IsDigit(phone.Text[phone.Text.Length - 1]))
                        {
                            phone.Text = phone.Text.Substring(0, phone.Text.Length - 1);
                        }
                    if (phone.Text != "" && phone.Text.Length < phoneCharLimit)
                    {
                        phoneOK = false;
                        DisplayAlert(AppRes.Attention, AppRes.Too_short_phone_number, AppRes.OK);
                        return;
                    }
                    else
                    {
                        phoneOK = true;
                    }
                    break;
                case "mail":
                    if(mail.Text != "" && !ValidateEmail(mail.Text))
                    {
                        mailOK = false;
                        DisplayAlert(AppRes.Attention, AppRes.Invalid_email, AppRes.OK);
                    }
                    else
                    {
                        mailOK = true;
                    }
                    break;
                case "password":
                    if(password.Text != "" && password.Text.Length < 8)
                    {
                        password.Text = "";
                        DisplayAlert(AppRes.Attention, AppRes.Password_must_contain_at_least_8_characters, AppRes.OK);
                    }
                    if(confirmPassword.Text != "" && confirmPassword.Text != password.Text)
                    {
                        password.Text = "";
                        DisplayAlert(AppRes.Attention, AppRes.Mismatched_passwords, AppRes.OK);
                    }
                    break;
                case "confirmpassword":
                    if (confirmPassword.Text != "" && confirmPassword.Text.Length < 8)
                    {
                        confirmPassword.Text = "";
                        DisplayAlert(AppRes.Attention, AppRes.Password_must_contain_at_least_8_characters, AppRes.OK);
                    }
                    if (password.Text != "" && confirmPassword.Text != password.Text)
                    {
                        confirmPassword.Text = "";
                        DisplayAlert(AppRes.Attention, AppRes.Mismatched_passwords, AppRes.OK);
                    }
                    break;
                default:
                    break;
            }
        }

        Regex EmailRegex = new Regex(@"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$");
        public bool ValidateEmail(string email)
        {
            if (string.IsNullOrWhiteSpace(email))
                return false;

            return EmailRegex.IsMatch(email);
        }

        async public void ConfirmCode(object sender, EventArgs e)
        {
            if (code.Text == "")
                await DisplayAlert(AppRes.Attention, AppRes.You_must_enter_verification_code, AppRes.OK);
            //code sent
            var verCode = new VerificationCode {
                mail = mail.Text,
                code = code.Text
            };

            //if there is time left
            //string result = await RequestStuff.doRequest("moto", JsonConvert.SerializeObject(verCode));

            if (false)//some server responce
            {
                await DisplayAlert(AppRes.Error, AppRes.Wrong_verification_code, AppRes.OK);
                code.Text = "";
                code.IsEnabled = false;
                code.IsVisible = false;
                sendCode.IsVisible = false;
                sendCode.IsEnabled = false;


                ToStart(sender, e);
                return;
            }
            code.IsEnabled = false;
            ToAccount();
        }
    }

    public class MaskedBehavior : Behavior<Entry>
    {
        private string _mask = "";
        public string Mask
        {
            get => _mask;
            set
            {
                _mask = value;
                SetPositions();
            }
        }

        protected override void OnAttachedTo(Entry entry)
        {
            entry.TextChanged += OnEntryTextChanged;
            base.OnAttachedTo(entry);
        }

        protected override void OnDetachingFrom(Entry entry)
        {
            entry.TextChanged -= OnEntryTextChanged;
            base.OnDetachingFrom(entry);
        }

        IDictionary<int, char> _positions;

        void SetPositions()
        {
            if (string.IsNullOrEmpty(Mask))
            {
                _positions = null;
                return;
            }

            var list = new Dictionary<int, char>();
            for (var i = 0; i < Mask.Length; i++)
                if (Mask[i] != 'X')
                    list.Add(i, Mask[i]);

            _positions = list;
        }

        private void OnEntryTextChanged(object sender, TextChangedEventArgs args)
        {
            var entry = sender as Entry;

            var text = entry.Text;

            if (string.IsNullOrWhiteSpace(text) || _positions == null)
                return;

            if (text.Length > _mask.Length)
            {
                entry.Text = text.Remove(text.Length - 1);
                return;
            }

            foreach (var position in _positions)
                if (text.Length >= position.Key + 1)
                {
                    var value = position.Value.ToString();
                    if (text.Substring(position.Key, 1) != value)
                        text = text.Insert(position.Key, value);
                }
            if (entry.Text != text)
                entry.Text = text;
        }

       
    }
}