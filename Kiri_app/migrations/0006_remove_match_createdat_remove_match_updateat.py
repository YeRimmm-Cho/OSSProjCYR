# Generated by Django 4.2.2 on 2024-06-04 15:14

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('Kiri_app', '0005_remove_match_matchscore'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='match',
            name='createdAt',
        ),
        migrations.RemoveField(
            model_name='match',
            name='updateAt',
        ),
    ]
