using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ScooterSharing;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using ZXing.Mobile;
using Xamarin.Forms;
using System.Threading.Tasks;

[assembly: Dependency(typeof(ScooterSharing.Droid.QrScanningService))]

namespace ScooterSharing.Droid
{
    public class QrScanningService : IQrScannerService
    {
        public async Task<string> ScanAsync()
        {
            var optionsDefault = new MobileBarcodeScanningOptions();
            var optionsCustom = new MobileBarcodeScanningOptions();

            var scanner = new MobileBarcodeScanner()
            {
                TopText = AppRes.Scan_QR_to_rent_scooter,
                BottomText = AppRes.Please_wait,
            };

            var scanResult = await scanner.Scan(optionsCustom);
            if (scanResult == null)
                return null;
            return scanResult.Text;
        }
    }
}