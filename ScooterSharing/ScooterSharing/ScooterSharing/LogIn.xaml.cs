using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.ComponentModel;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace ScooterSharing
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class LogIn : ContentPage
    {
        private int phoneCharLimit = 11;
        private int fsNameCharLimit = 40;
        private enum UIMode{Default,LogIn,SignUp };
        int onFinishCheck; // since both click and close button's animation have to be played (for draw on last frame)
        int onFinishClose; // during moving to input mode both onFinish methods call before they actually will
                           // need, there is a need in counter of OnFinish calls for both OnFinish methods

        UIMode uiMode;
        public LogIn()
        {
            InitializeComponent();
            uiMode = UIMode.Default;
            onFinishClose = 0;
            onFinishCheck = 0;
        }

        public void MoveToSignUp(object sender, EventArgs e)
        {
            uiMode = UIMode.SignUp;

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
            errorLabel.IsEnabled = false;

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
            errorLabel.IsEnabled = false;

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
            ToAccount();//temporary!!

            check.Play();
            
            if(uiMode == UIMode.SignUp && (fName.Text == "" || sName.Text == "" || phone.Text == "" || mail.Text == "" ||
                password.Text == "" || confirmPassword.Text == ""))
            {
                errorLabel.Text = "Fill all fields first!";
                return;
            }

            if (uiMode == UIMode.LogIn && (mail.Text == "" || password.Text == ""))
            {
                errorLabel.Text = "Fill all fields first!";
                return;
            }

            if (uiMode == UIMode.SignUp && confirmPassword.Text != password.Text)
            {
                errorLabel.Text = "Mismatched passwords!";
                return;
            }

            //ToAccount();
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
            check.IsEnabled = false;
            close.IsEnabled = false;
            uiMode = UIMode.Default;
        }

        public void ToStartFromSignUp()
        {
            btnSignUp.TranslateTo(0, 0, 600, Easing.BounceOut);
            btnSignUp.ScaleTo(1, 650, Easing.SinOut);
            btnSignUp.FadeTo(1, 650, Easing.Linear);

            btnSignUp.IsEnabled = true;
            btnLogIn.IsEnabled = true;
            errorLabel.IsEnabled = true;

            btnLogIn.FadeTo(1, 100, Easing.Linear);
            fName.FadeTo(0, 100, Easing.Linear);
            sName.FadeTo(0, 200, Easing.Linear);
            phone.FadeTo(0, 300, Easing.Linear);
            mail.FadeTo(0, 400, Easing.Linear);
            password.FadeTo(0, 500, Easing.Linear);

            confirmPassword.FadeTo(0, 600, Easing.Linear);
            check.FadeTo(0, 600, Easing.Linear);
            close.FadeTo(0, 600, Easing.Linear);

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
        }
        public void ToStartFromLogin()
        {
            btnLogIn.TranslateTo(0, 0, 600, Easing.BounceOut);
            btnLogIn.ScaleTo(1, 650, Easing.SinOut);
            btnLogIn.FadeTo(1, 650, Easing.Linear);

            btnSignUp.IsEnabled = true;
            btnLogIn.IsEnabled = true;
            errorLabel.IsEnabled = true;

            mail.IsEnabled = false;
            mail.IsVisible = false;
            password.IsEnabled = false;
            password.IsVisible = false;

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

        public void ToAccount()
        {
            TabbedPage tabbedPage = new TabbedPage();
            tabbedPage.Children.Add(new Account());
            tabbedPage.Children.Add(new XMap());
            tabbedPage.BarBackgroundColor = Color.Green;
            Application.Current.MainPage = tabbedPage;
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
                    if (phone.Text.Length > phoneCharLimit)
                    {
                        phone.Text = phone.Text.Substring(0, phoneCharLimit);
                    }
                    break;
                case "mail":
                    break;
                case "password":
                    break;
                case "confirmpassword":
                    break;
                default:
                    break;
            }
            
        }
    }
}