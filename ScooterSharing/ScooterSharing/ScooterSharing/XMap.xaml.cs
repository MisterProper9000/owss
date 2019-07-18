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

namespace ScooterSharing
{   
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class XMap : ContentPage
    {
        public IList<Scooter> Scooters { get; private set; }
        private Location curLoc;
        private int latLongDigitNumber = 10;
        private string mapsRequest;
        bool isRouteMode;
        public XMap()
        {
            mapsRequest = "https://www.google.ru/maps/";
            isRouteMode = false;
            //https://www.google.ru/maps/dir/59.9608485,30.3303026/59.9648886,30.3454112/
            InitializeComponent();
            SetLoc();
            Browser.TranslationY = -65;
            Browser.Source = mapsRequest;
            Scooters = new List<Scooter>();
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
                Name = "CoolRentCompany9000",
                ImageSource = "http://upload.wikimedia.org/wikipedia/commons/thumb/8/83/BlueMonkey.jpg/220px-BlueMonkey.jpg",
                Price="2$/h"
            });
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                Name = "AnotherRentCompany9000",
                ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                Price = "2.5$/h"
            });
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                Name = "AnotherRentCompany9000",
                ImageSource = "ScooterSharing.scooter.png",
                Price = "2.5$/h"
            });
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                Name = "AnotherRentCompyyuujjkkllooany9000",
                ImageSource = "ScooterSharing.back.jpg",
                Price = "2.5$/h"
            });
            Scooters.Add(new Scooter
            {
                fuelLvl = generator.NextDouble(),
                Lat = 59.954833,
                Lng = 30.337149,
                Name = "AnotherRentCompany9000",
                ImageSource = "ScooterSharing/back.jpg",
                Price = "2.5$/h"
            });
            BindingContext = this;

            
        }

        private void CutPos(Location loc)
        {
            string[] str = loc.Latitude.ToString().Split('.');
            loc.Latitude = Math.Round(loc.Latitude, Math.Max(0, 10 - str[0].Length));
            str = loc.Longitude.ToString().Split('.');
            loc.Longitude = Math.Round(loc.Longitude, Math.Max(0, 10 - str[0].Length));
        }

        async private void SetLoc()
        {
            curLoc = await Geolocation.GetLastKnownLocationAsync();
        }

        public void OnScooterSelected(object sender, SelectedItemChangedEventArgs e)
        {
            Scooter selectedItem = e.SelectedItem as Scooter;
        }
    
        public void OnScooterTapped(object sender, ItemTappedEventArgs e)
        {
            Scooter tappedItem = e.Item as Scooter;
            if (curLoc != null)
            {
                dummyMap.IsEnabled = false;
                dummyMap.IsVisible = false;
                Browser.IsEnabled = true;
                Browser.IsVisible = true;
                Browser.Source = mapsRequest + "dir/" + curLoc.Latitude.ToString() + "," + curLoc.Longitude.ToString() + "/" + tappedItem.Lat + "," + tappedItem.Lng + "/";
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


    }
}