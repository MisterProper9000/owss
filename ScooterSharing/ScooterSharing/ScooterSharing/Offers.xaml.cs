using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace ScooterSharing
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class Offers : ContentPage
    {
        public Offers()
        {
            InitializeComponent();
            //NavigationPage.SetHasNavigationBar(this, false);
        }
    }
}