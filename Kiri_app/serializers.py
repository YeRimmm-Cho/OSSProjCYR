#DRF에서 직렬화를 위해 사용되는 파일

#Django에서 데이터 직렬화를 지원하는 기능을 제공합니다.
#데이터 직렬화는 데이터를 다른 형식(예: JSON 또는 XML)으로 변환하는 과정을 말합니다
from rest_framework import serializers
from .models import Room, AppUser, Chat

from django.db import models

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = AppUser
        fields = ("__all__")

class RoomSerializer(serializers.ModelSerializer):
    class Meta:
        model = Room
        fields = ("__all__")

class ChatSerializer(serializers.ModelSerializer):
    class Meta:
        model = Chat
        fields = ("__all__")


# KSH: ProfileSerializer 추가
class ProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Profile
        fields = ("__all__")

# KSH: UserPrefSerializer 추가
class UPrefSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.UserPref
        fields = ("__all__")

# KSH: MatchSerializer 추가
class MatchSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Match
        fields = ("__all__")

# KSH: RoomSerializer 추가
class ReportSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Report
        fields = ("__all__")

# KSH: BlockSerializer 추가
class BlockSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Block
        fields = ("__all__")