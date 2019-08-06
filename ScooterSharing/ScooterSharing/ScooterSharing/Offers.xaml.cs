using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace ScooterSharing
{
    public class Tariff
    {
        public string m_text { get; set; }
        public double m_time { get; set; }
        public string s_time { get; set; }
        public string m_currency { get; set; }
        public double m_money { get; set; }//by now dollars only

        public Tariff(double money, string currency, string stime, double time)
        {
            m_money = money;
            m_time = time;
            s_time = stime;
            m_currency = currency;
            m_text = AppRes.Tariff + ": " + m_money + " " + m_currency + "/" + s_time;
        }
    }

    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class Offers : ContentPage
    {
        public ObservableCollection<Tariff> tariffs { get; private set; }
        public Offers()
        {
            InitializeComponent();
            ((NavigationPage)Application.Current.MainPage).BarBackgroundColor = Color.Green;
            tariffs = new ObservableCollection<Tariff>();
            tariffs.Add(new Tariff(1, "USD", "m", 1));
            tariffs.Add(new Tariff(59.99, "USD", "h", 60));
            BindingContext = this;
            
            //App.Current.Properties["tariff"] = tariffs[0].m_money.ToString()+"|"+ tariffs[0].m_time.ToString();
            App.Current.SavePropertiesAsync();
            foreach(var e in tariffs)
            {
                if (e.m_money.ToString()+"|"+e.m_time.ToString() == App.Current.Properties["tariff"].ToString())
                {
                    TariffList.SelectedItem = e;
                }
            }
            //NavigationPage.SetHasNavigationBar(this, false);
        }
        async private void TariffTapped(object sender, SelectedItemChangedEventArgs e)
        {
            Tariff selectedItem = e.SelectedItem as Tariff;
            App.Current.Properties["tariff"] = selectedItem.m_money.ToString()+"|"+selectedItem.m_time.ToString();
            App.Current.Properties["showtariff"] = selectedItem.m_text;
            await App.Current.SavePropertiesAsync();
        }

        private bool _isRefreshing = false;
        public bool IsRefreshing
        {
            get { return _isRefreshing; }
            set
            {
                _isRefreshing = value;
                OnPropertyChanged(nameof(IsRefreshing));
            }
        }
        public ICommand RefreshCommand
        {
            get
            {
                return new Command(async () =>
                {
                    IsRefreshing = true;

                    await Task.Delay(2000);

                    IsRefreshing = false;
                });
            }
        }

    }
}