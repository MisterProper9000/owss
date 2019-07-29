using System;
using System.Collections.Generic;
using System.Text;

namespace ScooterSharing
{
    public class Scooter
    {
        public double fuelLvl { get; set; }
        public double Lat { get; set; }
        public double Lng { get; set; }
        public string Name { get; set; }
        public string Price { get; set; }
        public string ImageSource { get; set; }

        public override string ToString()
        {
            return Name;
        }
    }
}
