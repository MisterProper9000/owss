using System;
using System.Collections.Generic;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Xamarin.Essentials;

using System.Collections.ObjectModel;
using System.Windows.Input;
using Newtonsoft.Json;

namespace ScooterSharing
{
    public class ScootersFromServer
    {
        public int id { get; set; }
        public string auto_number { get; set; }
        public string model { get; set; }
        public int idowner { get; set; }
        public bool status_rent { get; set; }
        public bool status_reserv { get; set; }

    }
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class XMap : ContentPage
    {
        public string btnResText { get; set; }
        private IList<object> pins { get; set; }
        private object selected { get; set; }
        public ObservableCollection<Scooter> Scooters { get; set; }
        public IList<ScootersFromServer> scootersFromServers { get; set; }
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
            //Random generator = new Random();
            map.Source = ImageSource.FromResource("ScooterSharing.map.png");
            //here some server responce processing (Lat & Lng maybe)
            if (curLoc != null)
                CutPos(curLoc); //cut digits from coordinates for one-lined view
            //can't bind images -- local and by url :(
            
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
                if (i != idx && Scooters[i].color != Color.WhiteSmoke && Scooters[i].color != Color.Gold)
                {
                    Scooter tmp = Scooters[i];
                    Scooters.Remove(Scooters[i]);
                    tmp.color = Color.WhiteSmoke;
                    tmp.reservable = false;
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
                    if(tmp.color != Color.Gold)
                        tmp.color = Color.YellowGreen;
                    if(App.Current.Properties["res"].ToString() == "no")
                        tmp.reservable = true;
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

        async private void Reserve(object sender, EventArgs e)
        {
            if(App.Current.Properties["res"].ToString() != "no")
            {
                string result1 = await RequestStuff.doRequest("checkresstat", App.Current.Properties["resId"].ToString());

                if (result1.Split('|')[0] != RequestResult.OK.ToString())
                    return;
                if (result1.Split('|')[1] == "false" && App.Current.Properties["res"].ToString() != "no")
                {
                    App.Current.Properties["res"] = "no";
                    await App.Current.SavePropertiesAsync();
                    await DisplayAlert("Attention", "You reservation was expired", "OK");
                    Console.WriteLine("PEREUSMAMS " + result1 + " " + App.Current.Properties["res"].ToString());
                    await RefreshScooters();
                    return;
                }           
                
                Console.WriteLine("ifnotno");
                await RequestStuff.doRequest("motoResCanc", App.Current.Properties["resId"].ToString()+"|"+App.Current.Properties["email"].ToString());
                App.Current.Properties["res"] = "no";
                await App.Current.SavePropertiesAsync();
                for(int k = 0; k < Scooters.Count; k++)
                {
                    if (Scooters[k].color == Color.Gold)
                    {
                        Scooter tmp = Scooters[k];
                        Scooters.Remove(Scooters[k]);
                        tmp.color = Color.WhiteSmoke;
                        tmp.reservable = false;
                        tmp.btnResText = AppRes.Reserve;
                        int sas = Math.Min(2, k);
                        ((Image)grid.FindByName("customPin1")).Scale = 0.03;
                        Scooters.Insert(k, tmp);
                        break;
                    }
                };
                scooterList.SelectedItem = null;
                return;
            }

            Scooter scooter = scooterList.SelectedItem as Scooter;
            Console.WriteLine("saskeksas" + scooter.id + "|" + App.Current.Properties["email"].ToString());
            
            string result = await RequestStuff.doRequest("motoRes", scooter.id+"|"+App.Current.Properties["email"].ToString());
            Console.WriteLine("saskeksas"+result);
            string[] parseRes = result.Split('|');

            if (parseRes[0] == RequestResult.NOTENOUGH.ToString())
            {
                await DisplayAlert(AppRes.Attention, AppRes.Not_enough_money_for_deposit, AppRes.OK);
                return;
            }
            if (parseRes[1] != "true")
            {
                await DisplayAlert(AppRes.Attention, AppRes.This_scooter_already_rented_or_reserved, AppRes.OK);
                return;
            }
            if (parseRes[0] != RequestResult.OK.ToString())
            {
                await DisplayAlert(AppRes.Attention, AppRes.Something_goes_wrong, AppRes.OK);
                return;
            }

            App.Current.Properties["resId"] = parseRes[2];
            await App.Current.SavePropertiesAsync();
            
            int idx = Scooters.IndexOf(scooter);
            for(int i = 0; i < Scooters.Count; i++)
            {
                Console.WriteLine(i);
                if (idx == i)
                {
                    Scooter tmp = Scooters[i];
                    Scooters.Remove(Scooters[i]);
                    tmp.color = Color.Gold;
                    tmp.reservable = true;
                    tmp.btnResText = AppRes.Unreserve;
                    Scooters.Insert(i, tmp);
                    break;
                }
            };
            ((Image)grid.FindByName("customPin1")).Scale += 0.02;
            App.Current.Properties["res"] = scooter.scoo.id;
            await App.Current.SavePropertiesAsync();
            Console.WriteLine("end");
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
        async private Task RefreshScooters()
        {
            if (App.Current.Properties["res"].ToString() != "no")
            {
                string result1 = await RequestStuff.doRequest("checkresstat", App.Current.Properties["resId"].ToString());

                if (result1.Split('|')[0] != RequestResult.OK.ToString())
                    return;
                if (result1.Split('|')[1] == "false" && App.Current.Properties["res"].ToString() != "no")
                {
                    App.Current.Properties["res"] = "no";
                    await App.Current.SavePropertiesAsync();
                    await DisplayAlert("Attention", "You reservation was expired", "OK");
                    Console.WriteLine("PEREUSMAMS " + result1 + " " + App.Current.Properties["res"].ToString());
                }
            }


            Color col = Color.Black;
            int idx = -1;
            if (Scooters.Count != 0 && scooterList.SelectedItem != null)
            {
                Scooter sc = scooterList.SelectedItem as Scooter;
                idx = sc.scoo.id;
                col = sc.color;
            }

            Scooters.Clear();
            bool res = false;
            bool clear = false;
            string result = await RequestStuff.doRequest("listmotomobile", "sas");
            List<ScootersFromServer> scootersFromServers = JsonConvert.DeserializeObject<List<ScootersFromServer>>(result);
            for (int i = 0; i < scootersFromServers.Count; i++)
            {
               /* if (App.Current.Properties["res"].ToString() == "no" && (!scootersFromServers[i].status_rent ||!scootersFromServers[i].status_reserv))
                {
                    Scooters.Add(new Scooter
                    {
                        scoo = scootersFromServers[i],
                        Price = App.Current.Properties["showtariff"].ToString(),
                        Lat = 59.954833,
                        Lng = 30.337149,
                        btnResText = AppRes.Reserve,
                        id = "sfb_moto:" + scootersFromServers[i].id,
                        color = Color.WhiteSmoke,
                        ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                        CompanyName = "CoolRentCompany9000",
                        reservable = false,
                    });
                    ((Image)grid.FindByName("customPin1")).Scale = 0.02;
                    continue;
                }*/
                Console.WriteLine("blyat " + App.Current.Properties["res"].ToString() + " " + scootersFromServers[i].id.ToString());
                if (App.Current.Properties["res"].ToString() != scootersFromServers[i].id.ToString())
                {
                    if (scootersFromServers[i].status_rent || scootersFromServers[i].status_reserv)
                    {
                        Console.WriteLine("countcount " + scootersFromServers[i].id);
                        continue;
                    }

                }
                else if (!scootersFromServers[i].status_reserv && App.Current.Properties["res"].ToString() == scootersFromServers[i].id.ToString())
                {
                    Console.WriteLine("GOVNO");
                    App.Current.Properties["res"] = "no";
                    await App.Current.SavePropertiesAsync();
                    clear = true;
                }
                else if (App.Current.Properties["res"].ToString() == "no")
                {
                    clear = true;
                }
                else
                    res = true;
                if (clear)
                { 
                    Scooters.Add(new Scooter
                    {
                        scoo = scootersFromServers[i],
                        Price = App.Current.Properties["showtariff"].ToString(),
                        Lat = 59.954833,
                        Lng = 30.337149,
                        btnResText = AppRes.Reserve,
                        id = "sfb_moto:" + scootersFromServers[i].id,
                        color = Color.WhiteSmoke,
                        ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                        CompanyName = "CoolRentCompany9000",
                        reservable = false,
                    });
                    ((Image)grid.FindByName("customPin1")).Scale = 0.03;
                    continue;
                }
                if (res)
                {
                    Scooters.Add(new Scooter
                    {
                        scoo = scootersFromServers[i],
                        Price = App.Current.Properties["showtariff"].ToString(),
                        Lat = 59.954833,
                        Lng = 30.337149,
                        btnResText = AppRes.Unreserve,
                        id = "sfb_moto:" + scootersFromServers[i].id,
                        color = Color.Gold,
                        ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                        CompanyName = "CoolRentCompany9000",
                        reservable = true,
                    });
                    ((Image)grid.FindByName("customPin1")).Scale += 0.02;
                }
                else
                    Scooters.Add(new Scooter
                    {
                        scoo = scootersFromServers[i],
                        Price = App.Current.Properties["showtariff"].ToString(),
                        Lat = 59.954833,
                        Lng = 30.337149,
                        btnResText = (idx == -1) ? AppRes.Reserve : (idx == scootersFromServers[i].id && col == Color.Gold) ? AppRes.Unreserve : AppRes.Reserve,
                        id = "sfb_moto:" + scootersFromServers[i].id,
                        color = (idx == -1) ? Color.WhiteSmoke : (idx == scootersFromServers[i].id) ? col : Color.WhiteSmoke,
                        ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png",
                        CompanyName = "CoolRentCompany9000",
                        reservable = (idx == -1) ? false : (idx == scootersFromServers[i].id) ? true : false,
                    });
            }
            Console.WriteLine("countcount " + scootersFromServers.Count + " " + Scooters.Count);
            res = false;
        }
    
        public void OnScooterTapped(object sender, ItemTappedEventArgs e)
        {
            Scooter tappedItem = e.Item as Scooter;

            int idx = Scooters.IndexOf(tappedItem);
            int i = 0;
            foreach (var it in scooterList.ItemsSource)
            {
                if (i != idx && Scooters[i].color != Color.WhiteSmoke && Scooters[i].color != Color.Gold)
                {
                    Scooter tmp = Scooters[i];
                    Scooters.Remove(Scooters[i]);
                    tmp.color = Color.WhiteSmoke;
                    tmp.reservable = false;
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
                    if(tmp.color != Color.Gold)
                        tmp.color = Color.YellowGreen;
                    if(App.Current.Properties["res"].ToString() == "no")
                        tmp.reservable = true;
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
                DisplayAlert(AppRes.Attention, AppRes.Location_unavailable, AppRes.Got_it);
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

                int i = 0;
                foreach (var it in scooterList.ItemsSource)
                {
                    if (Scooters[i].color != Color.WhiteSmoke && Scooters[i].color != Color.Gold)
                    {
                        Scooter tmp = Scooters[i];
                        Scooters.Remove(Scooters[i]);
                        tmp.color = Color.WhiteSmoke;
                        tmp.reservable = false;                        
                        Scooters.Insert(i, tmp);
                        break;
                    }
                    i++;
                };

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

                    await RefreshScooters();

                    IsRefreshing = false;
                });
            }
        }
        async protected override void OnAppearing()
        {
            await RefreshScooters();

            /*for (int i =0; i < Scooters.Count; i++)
            {
                Scooter tmp = Scooters[i];
                Scooters.Remove(Scooters[i]);
                tmp.Price = App.Current.Properties["showtariff"].ToString();
                
                if (App.Current.Properties["res"].ToString() == "no")
                {
                    tmp.color = Color.WhiteSmoke;
                    tmp.reservable = false;
                    tmp.btnResText = AppRes.Reserve;                    
                }
                else if(i.ToString() == App.Current.Properties["res"].ToString())
                {
                    tmp.color = Color.Gold;
                    tmp.reservable = true;
                    tmp.btnResText = "Unreserve";
                }
                if (tmp.color == Color.Yellow)
                    tmp.color = Color.WhiteSmoke;
                Scooters.Insert(i, tmp);
            }*/
            
            base.OnAppearing();
        }



    }
}

