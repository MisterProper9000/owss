using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace ScooterSharing
{
    public class PaymentsViewModel
    {
        public ObservableCollection<Payment> Payments { get; set; }
        public PaymentsViewModel()
        {
            IList<Child> sas = new List<Child>()
            {
                new Child
                {
                    txt = "sas",
                },
                new Child
                {
                    txt = "kek",
                },
                new Child
                {
                    txt = "sas",
                },
                new Child
                {
                    txt = "kek",
                },
                new Child
                {
                    txt = "sas",
                },
                new Child
                {
                    txt = "kek",
                }
            };

            IList<Child> sas1 = new List<Child>()
            {
                new Child
                {
                    txt = "wefwf",
                },
            };

            Payments = new ObservableCollection<Payment>
            {
                new Payment("CoolrentCompany", "22$", "21.92.1443", "65.13.5355", sas),
                new Payment("CoolrentCompany", "12$", "86.12.1314", "13.52.2425", sas1)
            };
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
