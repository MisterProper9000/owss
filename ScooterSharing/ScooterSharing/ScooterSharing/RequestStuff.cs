using System;
using System.Net.Http;
using System.Net;
using System.Threading.Tasks;
using Xamarin.Forms;

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
        RENTALREADYSTARTED,
        NOTENOUGH,
        BLOCKED,
        ERROR,
        OTHER
    }
    
    public class PaymentRequest
    {
        public string fName { get; set; }
        public string lName { get; set; }
        public string cardNum { get; set; }
        public string cvc2 { get; set; }
        public string exDate { get; set; }
        public string sum { get; set; }
    }
    public class QRstart
    {
        public string period { get; set; }
        public string tariff { get; set; }
        public string email { get; set; }
        public string qr { get; set; }
    }
    public class VerificationCode
    {
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

    public class Scooter
    {
        public ScootersFromServer scoo { get; set; }
        public double Lat { get; set; }
        public double Lng { get; set; }
        public string CompanyName { get; set; }
        public string Price { get; set; }
        public string ImageSource { get; set; }
        public Color color { get; set; }
        public bool reservable { get; set; }
        public string btnResText { get; set; }
        public string id { get; set; }

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
        public static readonly HttpClient httpClient = new HttpClient { BaseAddress = new Uri("http://10.101.177.12:9091/") };
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
