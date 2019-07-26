using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace ScooterSharing
{
    public interface IQrScannerService
    {
        Task<string> ScanAsync();
    }
}
