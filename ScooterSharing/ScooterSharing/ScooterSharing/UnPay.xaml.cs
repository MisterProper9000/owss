using System;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace ScooterSharing
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class UnPay : ContentPage
    {
        private int flNameCharLimit = 40;
        private int cardNumCharLimit = 19;
        private int cvc2CharLimit = 3;
        public UnPay()
        {
            InitializeComponent();
            //NavigationPage.SetHasNavigationBar(this, false);
            epay.Source = ImageSource.FromResource("ScooterSharing.epay.png");
        }
        public void DetectTrash(object sender, EventArgs e)
        {
            var entry = (Entry)sender;
            switch (entry.ClassId)
            {
                case "fName":
                    if (fName.Text.Length > 0)
                        if (!Char.IsLetter(fName.Text[fName.Text.Length - 1]))
                        {
                            fName.Text = fName.Text.Substring(0, fName.Text.Length - 1);
                        }
                    if (fName.Text.Length > flNameCharLimit)
                    {
                        fName.Text = fName.Text.Substring(0, flNameCharLimit);
                    }
                    break;
                case "sName":
                    if (lName.Text.Length > 0)
                        if (!Char.IsLetter(lName.Text[lName.Text.Length - 1]))
                        {
                            lName.Text = lName.Text.Substring(0, lName.Text.Length - 1);
                        }
                    if (lName.Text.Length > flNameCharLimit)
                    {
                        lName.Text = lName.Text.Substring(0, flNameCharLimit);
                    }
                    break;
                case "cardNum":
                    if (cardNum.Text.Length > 0)
                        if (!Char.IsDigit(cardNum.Text[cardNum.Text.Length - 1]))
                        {
                            cardNum.Text = cardNum.Text.Substring(0, cardNum.Text.Length - 1);
                        }
                    break;
                case "cvc2":
                    if (cvc2.Text.Length > 0)
                        if (!Char.IsDigit(cvc2.Text[cvc2.Text.Length - 1]))
                        {
                            cvc2.Text = cvc2.Text.Substring(0, cvc2.Text.Length - 1);
                        }
                    if (cvc2.Text.Length > cvc2CharLimit)
                    {
                        cvc2.Text = cvc2.Text.Substring(0, cvc2CharLimit);
                    }
                    break;
                case "payAmount":
                    if (payAmount.Text.Length > 0)
                        if (!Char.IsDigit(payAmount.Text[payAmount.Text.Length - 1]))
                        {
                            payAmount.Text = payAmount.Text.Substring(0, payAmount.Text.Length - 1);
                        }
                    break;
                case "exdate":
                    if (exdate.Text.Length > 0)
                        if (!Char.IsDigit(exdate.Text[exdate.Text.Length - 1]))
                        {
                            exdate.Text = exdate.Text.Substring(0, exdate.Text.Length - 1);
                        }
                    break;
                default:
                    break;
            }
        }
        private void CheckNumbers(object sender, EventArgs e)
        {
            var entry = (Entry)sender;
            switch (entry.ClassId)
            {
                case "cvc2":
                    if (cvc2.Text.Length < cvc2CharLimit)
                    {
                        cvc2.Text = "";
                        DisplayAlert(AppRes.Attention, AppRes.Too_short_code, AppRes.OK);
                    }
                    break;
                case "cardNum":
                    if (cardNum.Text.Length < cardNumCharLimit)
                    {
                        cardNum.Text = "";
                        DisplayAlert(AppRes.Attention, AppRes.Too_short_card_number, AppRes.OK);
                    }
                    break;
                case "exdate":
                    if (cardNum.Text.Length < 5)
                    {
                        cardNum.Text = "";
                        DisplayAlert(AppRes.Attention, AppRes.Too_short_code, AppRes.OK);
                    }
                    break;
                default:
                    break;
            }
        }
        async private void DoPay(object sender, EventArgs e)
        {
            if (cardNum.Text == "" || cvc2.Text == "" || exdate.Text == ""
                || payAmount.Text == "")
            {
                await DisplayAlert(AppRes.Attention, AppRes.All_fields_must_be_filled, AppRes.OK);
                return;
            }
            PaymentRequest pr = new PaymentRequest
            {
                cvc2 = cvc2.Text,
                cardNum = cardNum.Text,
                exDate = exdate.Text,
                sum = payAmount.Text
            };
            payAnim.IsEnabled = true;
            payAnim.IsVisible = true;
            payAnim.Play();
            string result = await RequestStuff.doRequest("topUpCl", pr.cardNum + "|" + pr.cvc2 + "|" + pr.exDate + "|" + pr.sum + "|" + App.Current.Properties["email"]);
            payAnim.Pause();
            payAnim.IsEnabled = false;
            payAnim.IsVisible = false;
            if(result.Split('|')[0] == RequestResult.OK.ToString())
                await DisplayAlert("Withdrawal was successful", "Money withdrawn: " + payAmount.Text+"$", AppRes.OK);
            else if (result.Split('|')[0] == RequestResult.ERROR.ToString())
                await DisplayAlert("Fail", "Wrong card data", AppRes.OK);
            cardNum.Text = "";
            cvc2.Text = "";
            payAmount.Text = "";
            exdate.Text = "";
        }
    }
}