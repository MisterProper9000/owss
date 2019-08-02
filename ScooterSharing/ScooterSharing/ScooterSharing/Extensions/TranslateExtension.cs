using System;
using System.Collections.Generic;
using System.Text;
using System.Resources;
using Plugin.Multilingual;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using System.Reflection;
using System.ComponentModel;

namespace ScooterSharing.Extensions
{
    public class Translator : INotifyPropertyChanged
    {
        public string this[string text]
        {
            get
            {
                return AppRes.ResourceManager.GetString(text, AppRes.Culture);
            }
        }

        public static Translator Instance { get; } = new Translator();

        public event PropertyChangedEventHandler PropertyChanged;

        public void Invalidate()
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(null));
        }
    }

    /* [ContentProperty("Text")]
     class TranslateExtension : IMarkupExtension
     {
         const string ResourceId = "ScooterSharing.AppRes";

         static readonly Lazy<ResourceManager> resmgr =
             new Lazy<ResourceManager>(() => new ResourceManager(ResourceId, typeof(TranslateExtension).GetTypeInfo().Assembly));

         public string Text { get; set; }

         public object ProvideValue(IServiceProvider serviceProvider)
         {
             if (Text == null)
                 return "";

             var ci = CrossMultilingual.Current.CurrentCultureInfo;
             var translation = resmgr.Value.GetString(Text, ci);

             if (translation == null)
             {
                 translation = Text; // returns the key, which GETS DISPLAYED TO THE USER
             }
             return translation;
         }


     }  */

    public class TranslateExtension : IMarkupExtension<BindingBase>
    {
        public TranslateExtension(string text)
        {
            Text = text;
        }

        public string Text { get; set; }

        object IMarkupExtension.ProvideValue(IServiceProvider serviceProvider)
        {
            return ProvideValue(serviceProvider);
        }

        public BindingBase ProvideValue(IServiceProvider serviceProvider)
        {
            var binding = new Binding
            {
                Mode = BindingMode.OneWay,
                Path = $"[{Text}]",
                Source = Translator.Instance,
            };
            return binding;
        }
    }
}
