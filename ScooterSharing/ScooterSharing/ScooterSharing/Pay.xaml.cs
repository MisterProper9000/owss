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
    public partial class Pay : ContentPage
    {
        private int flNameCharLimit = 40;
        private int cardNumCharLimit = 19;
        private int cvc2CharLimit = 3;
        public Pay()
        {
            InitializeComponent();
            //NavigationPage.SetHasNavigationBar(this, false);
            epay.Source = ImageSource.FromResource("ScooterSharing.epay.png");
            doPay.Source = ImageSource.FromResource("ScooterSharing.dopay.png");
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
                        DisplayAlert("Attention", "Too short cvc2", "OK");
                    break;
                case "cardNum":
                    if (cardNum.Text.Length < cardNumCharLimit)
                        DisplayAlert("Attention", "Too short card number", "OK");
                    break;
                default:
                    break;
            }
        }

        async private void DoPay(object sender, EventArgs e)
        {
            payAnim.IsEnabled = true;
            payAnim.IsVisible = true;
            payAnim.Play();
            await Task.Delay(2000);
            payAnim.Pause();
            payAnim.IsEnabled = false;
            payAnim.IsVisible = false;
        }
    }

    public class MaskedBehaviorCardNum : Behavior<Entry>
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