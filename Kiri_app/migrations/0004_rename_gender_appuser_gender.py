# Generated by Django 4.2.2 on 2024-06-01 10:53

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('Kiri_app', '0003_rename_id_appuser_id_rename_name_appuser_name_and_more'),
    ]

    operations = [
        migrations.RenameField(
            model_name='appuser',
            old_name='Gender',
            new_name='gender',
        ),
    ]
