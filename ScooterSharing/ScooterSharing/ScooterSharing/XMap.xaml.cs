using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Xamarin.Essentials;
using Android.Views;
using Xamarin.Forms.Internals;
using System.Reflection;

using System.Collections.ObjectModel;
using System.Windows.Input;

namespace ScooterSharing
{   
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class XMap : ContentPage
    {
        private IList<object> pins { get; set; }
        private object selected { get; set; }
        public ObservableCollection<Scooter> Scooters { get; private set; }
        private Location curLoc = null;
        private int latLongDigitNumber = 10;
        private string mapsRequest;
        bool isRouteMode;
        public XMap()
        {
            mapsRequest = "https://www.google.ru/maps/";// "http://10.101.177.12/9091/d";
            isRouteMode = false;
            //https://www.google.ru/maps/dir/59.9608485,30.3303026/59.9648886,30.3454112/
            InitializeComponent();
            SetLoc();
            Browser.TranslationY = -65;
            Browser.Source = mapsRequest;
            Scooters = new ObservableCollection<Scooter>();
            Random generator = new Random();
            map.Source = ImageSource.FromResource("ScooterSharing.map.png");
            //here some server responce processing (Lat & Lng maybe)
            if (curLoc != null)
                CutPos(curLoc); //cut digits from coordinates for one-lined view
            //can't bind images -- local and by url :(
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.9648886,
                Lng = 30.3454112,
                CompanyName = "CoolRentCompany9000",
                color = Color.WhiteSmoke,
                ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                Price ="2$/h"
            });
            selected = Scooters[0];
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                CompanyName = "AnotherRentCompany9000",
                color = Color.WhiteSmoke,
                ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                Price = "2.5$/h"
            });;
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                CompanyName = "AnotherRentCompany9000",
                color = Color.WhiteSmoke,
                ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                Price = "2.5$/h"
            });
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                CompanyName = "AnotherRentCompany6000",
                color = Color.WhiteSmoke,
                ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                Price = "2.5$/h"
            });
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                CompanyName = "AnotherRentCompany9000",
                color = Color.WhiteSmoke,
                ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                Price = "2.5$/h"
            });
            BindingContext = this;
            
            customPin1.Source = ImageSource.FromResource("ScooterSharing.pin.png");
            customPin2.Source = ImageSource.FromResource("ScooterSharing.pin.png");
            customPin3.Source = ImageSource.FromResource("ScooterSharing.pin.png");
            customPin1.TranslationX = -430;
            customPin1.TranslationY = -190;
            customPin2.TranslationX = -430-100;
            customPin2.TranslationY = -190+100;
            customPin3.TranslationX = -430-100;
            customPin3.TranslationY = -190-150;
            pins = new List<object>();
            pins.Add(customPin1);
            pins.Add(customPin2);
            pins.Add(customPin3);

        }

        private void PinTap(object sender, EventArgs e)
        {
            int idx = int.Parse(((Image)sender).ClassId);
            int i = 0;
            foreach (var it in scooterList.ItemsSource)
            {
                if (i != idx && Scooters[i].color != Color.WhiteSmoke)
                {
                    Scooter tmp = Scooters[i];
                    Scooters.Remove(Scooters[i]);
                    tmp.color = Color.WhiteSmoke;
                    Scooters.Insert(i, tmp);
                    break;
                }
                i++;
            };
            i = 0;
            foreach (var it in scooterList.ItemsSource)
            {
                if (idx == i)
                {
                    scooterList.ScrollTo(it, ScrollToPosition.Start, true);
                    scooterList.SelectedItem = it;
                    Scooter tmp = Scooters[i];
                    Scooters.Remove(Scooters[i]);
                    tmp.color = Color.YellowGreen;
                    Scooters.Insert(i, tmp);
                    return;
                }
                i++;
            };
            
        }

        private void CutPos(Location loc)
        {
            string[] str = loc.Latitude.ToString().Split('.');
            loc.Latitude = Math.Round(loc.Latitude, Math.Max(0, latLongDigitNumber - str[0].Length));
            str = loc.Longitude.ToString().Split('.');
            loc.Longitude = Math.Round(loc.Longitude, Math.Max(0, latLongDigitNumber - str[0].Length));
        }

        async private void SetLoc()
        {
            curLoc = await Geolocation.GetLocationAsync();
            if(curLoc == null)
                curLoc = await Geolocation.GetLastKnownLocationAsync();
        }

        public void OnScooterSelected(object sender, SelectedItemChangedEventArgs e)
        {
            Scooter selectedItem = e.SelectedItem as Scooter;
        }
    
        public void OnScooterTapped(object sender, ItemTappedEventArgs e)
        {
            Scooter tappedItem = e.Item as Scooter;

            int idx = Scooters.IndexOf(tappedItem);
            int i = 0;
            foreach (var it in scooterList.ItemsSource)
            {
                if (i != idx && Scooters[i].color != Color.WhiteSmoke)
                {
                    Scooter tmp = Scooters[i];
                    Scooters.Remove(Scooters[i]);
                    tmp.color = Color.WhiteSmoke;
                    Scooters.Insert(i, tmp);
                    break;
                }
                i++;
            };
            i = 0;
            foreach (var it in scooterList.ItemsSource)
            {
                if (idx == i)
                {
                    scooterList.ScrollTo(it, ScrollToPosition.Start, true);
                    scooterList.SelectedItem = it;
                    Scooter tmp = Scooters[i];
                    Scooters.Remove(Scooters[i]);
                    tmp.color = Color.YellowGreen;
                    Scooters.Insert(i, tmp);
                    break;
                }
                i++;
            };


            if (curLoc != null)
            {
                dummyMap.IsEnabled = false;
                dummyMap.IsVisible = false;
                Browser.IsEnabled = true;
                Browser.IsVisible = true;
                Browser.Source = mapsRequest + "dir/" + curLoc.Latitude.ToString().Replace(',', '.') + "," + curLoc.Longitude.ToString().Replace(',', '.') + "/" + tappedItem.Lat.ToString().Replace(',', '.') + "," + tappedItem.Lng.ToString().Replace(',', '.') + "/";
                isRouteMode = true;
            }
            else
                DisplayAlert("Attention", "Can't get device location\nRouting unavaliable", "Got it");
        }

        protected override bool OnBackButtonPressed()
        {
            if (isRouteMode)
            {
                dummyMap.IsEnabled = true;
                dummyMap.IsVisible = true;
                Browser.IsEnabled = false;
                Browser.IsVisible = false;
                Browser.Source = mapsRequest;
                return true;
            }
            else
                return base.OnBackButtonPressed();
        }

        //for ui dummy map
        /*async private void ButtonOpenCoords_Clicked(object sender, EventArgs e)
        {
            if (!double.TryParse(EntryLatitude.Text, out double lat))
                return;
            if (!double.TryParse(EntryLongitude.Text, out double lng))
                return;

            await Map.OpenAsync(lat, lng, new MapLaunchOptions
            {
                Name = EntryName.Text,
                NavigationMode = NavigationMode.Walking
            });
        }*/

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