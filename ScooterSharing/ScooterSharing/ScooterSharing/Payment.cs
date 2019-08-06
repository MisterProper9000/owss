using System;
using System.Collections.Generic;
using System.Text;

namespace ScooterSharing
{
    public class PaymentFromServer
    {
        public int id { get; set; }
        public string begin_time { get; set; }
        public string end_time { get; set; }
        public int idmoto { get; set; }
        public int idclient { get; set; }
        public float tariff { get; set; }
        public float tariff_time { get; set; }
        public string cost { get; set; }
        public string rrn { get; set; }

    }
    public class Payment
    {
        public Payment(string company, PaymentFromServer pay)
        {
            IsVisible = false;
            Company = "Tariff: " + pay.tariff + "$/" + ((pay.tariff_time == 1)?"m":"h"); //company;
            paymentData = pay;
            paymentData.cost += "$";
        }
        public string Company { get; set; }
        public bool IsVisible { get; set; }
        public PaymentFromServer paymentData { get; set; }

    }
}
