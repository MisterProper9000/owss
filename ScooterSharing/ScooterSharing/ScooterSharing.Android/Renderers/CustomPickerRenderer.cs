using System;
using System.Linq;
using Android.App;
using Android.Content;
using Android.Text;
using Android.Widget;
using Xamarin.Forms;
using Xamarin.Forms.Platform.Android;
using ScooterSharing.Renderers;
using ScooterSharing.Droid.Renderers;

[assembly: ExportRenderer(typeof(CustomPicker), typeof(CustomPickerRenderer))]
namespace ScooterSharing.Droid.Renderers
{
    public class CustomPickerRenderer : PickerRenderer
    {
        AlertDialog _dialog;
        IElementController ElementController => Element as IElementController;
        CustomPicker customPicker;
        public CustomPickerRenderer(Context context) : base(context)
        {
        }

        protected override void OnElementChanged(ElementChangedEventArgs<Picker> e)
        {
            if (e.NewElement == null) return;
            customPicker = e.NewElement as CustomPicker;
            if (e.NewElement != null)
            {
                if (Control == null)
                {
                    var textField = CreateNativeControl();
                    textField.SetOnClickListener(CustomPickerListener.Instance);
                    textField.InputType = InputTypes.Null;
                    SetNativeControl(textField);
                }
            }
            base.OnElementChanged(e);
        }

        public void OnClick()
        {
            Picker model = Element;

            var picker = new NumberPicker(Context);
            if (model.Items != null && model.Items.Any())
            {
                picker.MaxValue = model.Items.Count - 1;
                picker.MinValue = 0;
                picker.SetDisplayedValues(model.Items.ToArray());
                picker.WrapSelectorWheel = false;
                picker.DescendantFocusability = Android.Views.DescendantFocusability.BlockDescendants;
                picker.Value = model.SelectedIndex;
            }

            var layout = new LinearLayout(Context) { Orientation = Orientation.Vertical };
            layout.AddView(picker);

            ElementController.SetValueFromRenderer(VisualElement.IsFocusedPropertyKey, true);

            var builder = new AlertDialog.Builder(Context);
            builder.SetView(layout);
            builder.SetTitle(model.Title ?? "");
            builder.SetNegativeButton(customPicker.CancelButtonText ?? "Cancel", (s, a) =>
            {
                ElementController.SetValueFromRenderer(VisualElement.IsFocusedPropertyKey, false);
                _dialog = null;
            });
            builder.SetPositiveButton(customPicker.DoneButtonText ?? "Accept", (s, a) =>
            {
                ElementController.SetValueFromRenderer(Picker.SelectedIndexProperty, picker.Value);
                // It is possible for the Content of the Page to be changed on SelectedIndexChanged. 
                // In this case, the Element & Control will no longer exist.
                if (Element != null)
                {
                    if (model.Items.Count > 0 && Element.SelectedIndex >= 0)
                        Control.Text = model.Items[Element.SelectedIndex];
                    ElementController.SetValueFromRenderer(VisualElement.IsFocusedPropertyKey, false);
                }
                _dialog = null;
            });

            _dialog = builder.Create();
            _dialog.DismissEvent += (sender, args) =>
            {
                ElementController?.SetValueFromRenderer(VisualElement.IsFocusedPropertyKey, false);
            };
            _dialog.Show();
        }

        class CustomPickerListener : Java.Lang.Object, IOnClickListener
        {
            public static readonly CustomPickerListener Instance = new CustomPickerListener();

            public void OnClick(global::Android.Views.View v)
            {
                var renderer = v.Tag as CustomPickerRenderer;
                if (renderer == null)
                    return;

                renderer.OnClick();
            }
        }
    }
}