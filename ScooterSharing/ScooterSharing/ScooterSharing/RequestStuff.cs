using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Http;
using System.Net;
using Newtonsoft.Json;
using System.Threading.Tasks;
using System.Collections.ObjectModel;
using System.Linq;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Xamarin.Essentials;
using Android.Views;
using Xamarin.Forms.Internals;
using System.Reflection;

using System.Windows.Input;

namespace ScooterSharing
{ 
    public enum RequestType
    {
        SIGNUP,
        LOGIN,
        BALANCE,
        SCOOTERLIST,
        ADDMONEY,
        VERCODE
    }

    public enum RequestResult
    {
        OK,
        ALREADYEXIST,
        DOESNTEXIST,
        VERCODEMISMATCH,
        OTHER
    }
    
    public class Balance
    {
        public readonly string type = RequestType.BALANCE.ToString();
        public string mail { get; set; }
    }

    public class VerificationCode
    {
        public readonly string type = RequestType.VERCODE.ToString();
        public string code { get; set; }
        public string mail { get; set; }
    }

    public class UserSignUp
    {
        public string first_name { get; set; }
        public string last_name { get; set; }
        public string phone { get; set; }
        public string email { get; set; }
        public string password { get; set; }
    }

    public class UserLogIn
    {
        public string email { get; set; }
        public string password { get; set; }
    }

    public class UserPay
    {
        public readonly string type = RequestType.ADDMONEY.ToString();
        string cardNum;
        string mail;
    }

    public class Scooter
    {
        public double fuelLvl { get; set; }
        public double Lat { get; set; }
        public double Lng { get; set; }
        public string CompanyName { get; set; }
        public string Price { get; set; }
        public string ImageSource { get; set; }
        public Color color { get; set; }

        public override string ToString()
        {
            return CompanyName;
        }
        /*
        public Scooter()
        {
            Lat = 0;
            Lng = 0;
            CompanyName = "";
            ImageSource = "http://icons.iconarchive.com/icons/google/noto-emoji-travel-places/1024/42560-motor-scooter-icon.png";
            Price = "";
            fuelLvl = 0;
        }*/
    }

    public class ResponseLogin
    {
        public string type { get; set; }
        public string fName { get; set; }
        public string lName { get; set; }
        public string balance { get; set; }
    }

    public class RequestStuff
    {
        public static readonly HttpClient httpClient = new HttpClient { BaseAddress = new Uri("http://10.101.177.21:9091/") };
        async public static Task<string> doRequest(string source, string requestBody)
        {
            HttpRequestMessage request = new HttpRequestMessage();
            request.RequestUri = new Uri(httpClient.BaseAddress+source);
            request.Content = new StringContent(requestBody);
            request.Headers.Add("Accept", "application/json");

            HttpResponseMessage response = await httpClient.SendAsync(request);
            if (response.StatusCode == HttpStatusCode.OK)
            {
                HttpContent responseContent = response.Content;
                return await responseContent.ReadAsStringAsync();
            }
            else
            {
                return RequestResult.OTHER.ToString();
            }
        }
    }
}
