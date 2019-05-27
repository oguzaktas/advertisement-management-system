using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;

namespace ReklamYonetim
{
    
    public partial class Form1 : Form
    {
       
       
        IFirebaseConfig config = new FirebaseConfig
        {
            AuthSecret = "YeCsml5Prl30yTKq5ivJ6zaDmgsTLnAynPFLqLO4",
            BasePath= "https://advertisementmanagement-63a99.firebaseio.com/"
        };
        IFirebaseClient client;
        public Form1()
        {
            InitializeComponent();
       
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            client = new FireSharp.FirebaseClient(config);

            if (client == null)
            {
                MessageBox.Show("Bağlantı gerçekleşmedi");
            }

        }

        private async void button1_Click(object sender, EventArgs e)
        {
            FirebaseResponse rsp = await client.GetAsync("Counter/node");
            Counter get = rsp.ResultAs<Counter>();

            var Data = new Data
            {
                firmaID = (Convert.ToInt32(get.cntr) + 1).ToString(),
                firmaAdi = textBox2.Text,
                latitude = textBox3.Text,
                longitude = textBox1.Text,
                kampanyaIcerik = textBox4.Text,
                kategori = comboBox2.Text,
                kampanyaSuresi = textBox5.Text,
            };
            if (Data.firmaAdi == "" || Data.latitude == "" || Data.longitude == "" || Data.kampanyaIcerik == "" || Data.kampanyaSuresi == "" || Data.kategori == "")
            {
                MessageBox.Show("Lütfen Tüm Alanları Eksiksiz Doldurun.");
            }
            else
            {
                SetResponse response = await client.SetAsync("reklam/" + Data.firmaID, Data);
                Data result = response.ResultAs<Data>();

                MessageBox.Show("Veri başarıyla eklendi.");
                var obj = new Counter
                {
                    cntr = Data.firmaID
                };

                SetResponse response1 = await client.SetAsync("Counter/node", obj);
            }

        }
    }
}
