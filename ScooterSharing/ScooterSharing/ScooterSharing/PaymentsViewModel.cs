using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace ScooterSharing
{
    public class PaymentsViewModel
    {
        public ObservableCollection<Payment> Payments { get; set; }
        public IList<PaymentFromServer> paymentFromServers { get; set; }
        public PaymentsViewModel()
        {
            Payments = new ObservableCollection<Payment>();
        }

        async public void Update()
        {
            string result = await RequestStuff.doRequest("listrentonmobile", App.Current.Properties["email"].ToString());
            List<PaymentFromServer> obj = JsonConvert.DeserializeObject<List<PaymentFromServer>>(result);
            Console.WriteLine("ALIVEALIVEALIVE");
            Payments.Clear();
            
            for(int i = 0; i< obj.Count; i++)
            {
                Payments.Add(new Payment("CoolRentCompany", obj[i]));
            }
        }

        public void HideOrShowPayment(Payment payment)
        {
            /*if(_oldPayment == payment)
            {*/
            //click twice on the same item to hide it
            payment.IsVisible = !payment.IsVisible;
            UpdatePayments(payment);
            /*}
            else
            {
                payment.IsVisible = true;
                UpdatePayments(payment);
            }
            _oldPayment = payment;*/
        }

        private void UpdatePayments(Payment payment)
        {
            var index = Payments.IndexOf(payment);
            Payments.Remove(payment);
            Payments.Insert(index, payment);
        }


    }
}
