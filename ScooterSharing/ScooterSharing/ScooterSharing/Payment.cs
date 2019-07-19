using System;
using System.Collections.Generic;
using System.Text;

namespace ScooterSharing
{
    public class Child
    {
        public string txt { get; set; }
    }
    public class Payment
    {
        public Payment(string company, string price, string dateStart, string dateEnd, IList<Child> _list)
        {
            IsVisible = false;
            Company = company;
            DateStart = dateStart;
            DateEnd = dateEnd;
            Price = price;
            list = _list;
            height = 45 * list.Count;
        }
        public string Price { get; set; }
        public string Company { get; set; }
        public string DateStart { get; set; }
        public string DateEnd { get; set; }
        public bool IsVisible { get; set; }

        public IList<Child> list { get; set; }
        public int height { get; set; }
    }
}
