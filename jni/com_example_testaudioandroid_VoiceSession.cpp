#include "com_example_testaudioandroid_VoiceSession.h"

#include <stdlib.h>
#include <stdio.h>
#include "ServerError.h"
#include "voiceSession.h"
#include "voiceClient.h"
#ifdef __cplusplus
extern "C"
{
#endif
VoiceSession gVoiceSession;
JavaVM  *gJavaVm = NULL;
static jobject gObj=NULL;
void onServerEvent(int eventId,void* param)
{
	//
	JNIEnv *env;
	voiceClient* c = (voiceClient*)param;
	gJavaVm->AttachCurrentThread(&env, NULL);
	static jmethodID on_server_disconnect = NULL;
	static jmethodID on_player_join_room=NULL;
	static jmethodID on_player_left_room=NULL;
	static jmethodID on_player_begin_talk=NULL;
	static jmethodID on_player_stop_talk=NULL;
	jclass jc = env->GetObjectClass(gObj);
	if(eventId == 26)
	{


		on_server_disconnect = env->GetMethodID(jc,"OnServerDisconnect", "()V");

		if(on_server_disconnect != NULL)
		{
			env->CallVoidMethod(gObj,on_server_disconnect);

		}
	}
	if(eventId == 22)
	{
		on_player_join_room = env->GetMethodID(jc,"OnPlayerJoinRoom", "(II)V");

		if(on_player_join_room != NULL)
		{
			env->CallVoidMethod(gObj,on_player_join_room,c->player_id,c->room_id);

		}
	}
	if(eventId == 24)
	{
		on_player_left_room = env->GetMethodID(jc,"OnPlayerLeftRoom", "(II)V");

		if(on_player_left_room != NULL)
		{
			env->CallVoidMethod(gObj,on_player_left_room,c->player_id,c->room_id);

		}
	}
	if(eventId == 30)
	{
		on_player_begin_talk = env->GetMethodID(jc,"OnPlayerBeginTalk", "(I)V");

		if(on_player_begin_talk != NULL)
		{
			env->CallVoidMethod(gObj,on_player_begin_talk,c->player_id);

		}
	}
	if(eventId == 32)
	{
		on_player_stop_talk = env->GetMethodID(jc,"OnPlayerStopTalk", "(I)V");

		if(on_player_stop_talk != NULL)
		{
			env->CallVoidMethod(gObj,on_player_stop_talk,c->player_id);

		}
	}
	//
}
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *javaVm, void *reserved) {
	gJavaVm = javaVm;

	return JNI_VERSION_1_6;
}
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_Init
(JNIEnv * env, jobject obj, jstring addr, jshort port)
{

	//
	gObj= env->NewGlobalRef(obj);
	//
	const char *a = env->GetStringUTFChars(addr, 0);
	//

	// use your string
	int r = gVoiceSession.Init(a,port);
	gVoiceSession.SetServerEvent(onServerEvent);
	env->ReleaseStringUTFChars( addr, a);


	return r;
}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    Reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_testaudioandroid_VoiceSession_Reset
(JNIEnv * env, jobject obj)
{
	// use your string
	gVoiceSession.Reset();

}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    LoginServer
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_LoginServer
(JNIEnv * env, jobject obj, jint playerId)
{
	int res = gVoiceSession.LoginServer(playerId);
	return res;
}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    LoginOutServer
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_LoginOutServer
(JNIEnv * env, jobject obj ){
	int res =gVoiceSession.LoginOutServer();
	return res;
}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    CreateRoom
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_CreateRoom
(JNIEnv * env, jobject obj, jint roomId)
{
	int res =gVoiceSession.CreateRoom(roomId);
	return res;
}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    DestroyRoom
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_DestroyRoom
(JNIEnv * env, jobject obj, jint roomId)
{
	int res =gVoiceSession.DestroyRoom(roomId);
	return res;

}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    EnterRoom
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_EnterRoom
(JNIEnv * env, jobject obj, jint roomId)
{
	int res =gVoiceSession.EnterRoom(roomId);
	return res;
}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    LeaveRoom
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_LeaveRoom
(JNIEnv * env, jobject obj)
{
	int res =gVoiceSession.LeaveRoom();
	return res;
}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    GetRoomList
 * Signature: (Ljava/util/ArrayList;)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_GetRoomList
(JNIEnv * env, jobject obj, jintArray  rooms)
{
	vector<int> vs;
	int res = gVoiceSession.GetRoomList(&vs);
	if(res ==0)
	{
		//
		jboolean b;

		int* pd=(int*)env->GetPrimitiveArrayCritical(rooms,&b);
		jsize size=env->GetArrayLength(rooms);
		for(int i=0;i<vs.size();i++)
		{
			pd[i] =vs[i];
		}
		env->ReleasePrimitiveArrayCritical(rooms,pd,0);
		//
	}
	return res;
}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    SendData
 * Signature: ([BI)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_SendData
(JNIEnv * env, jobject obj, jbyteArray data, jint len)
{
	jboolean b;

	char* pd=(char*)env->GetPrimitiveArrayCritical(data,&b);
	int res =gVoiceSession.SendData(pd,len);
	env->ReleasePrimitiveArrayCritical(data,pd,0);
	return res;

}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    SetRecvData
 * Signature: (IZ)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_SetRecvData
(JNIEnv * env, jobject obj, jint playerId, jboolean recvData)
{
	int res = gVoiceSession.SetRecvData(playerId,recvData);
	return res;

}
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_BeginTalk
(JNIEnv * env, jobject obj)
{
	int res = gVoiceSession.BeginTalk();
	return res;

}
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_StopTalk
(JNIEnv * env, jobject obj)
{
	int res = gVoiceSession.StopTalk();
	return res;

}
/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    GetData
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_GetData
(JNIEnv * env, jobject obj, jbyteArray data)
{
	jboolean b;

	char* pd=(char*)env->GetPrimitiveArrayCritical(data,&b);
	int res =gVoiceSession.GetData(pd);
	env->ReleasePrimitiveArrayCritical(data,pd,0);
	return res;

}

/*
 * Class:     com_example_testaudioandroid_VoiceSession
 * Method:    GetRoomMember
 * Signature: (ILjava/util/ArrayList;)I
 */
JNIEXPORT jint JNICALL Java_com_example_testaudioandroid_VoiceSession_GetRoomMember
(JNIEnv * env, jobject obj, jint roomId,  jintArray  members)
{
	vector<int> vs;
	int res = gVoiceSession.GetRoomMember(roomId,&vs);
	if(res ==0)
	{
		//
		jboolean b;

		int* pd=(int*)env->GetPrimitiveArrayCritical(members,&b);
		jsize size=env->GetArrayLength(members);
		for(int i=0;i<vs.size();i++)
		{
			pd[i] =vs[i];
		}
		env->ReleasePrimitiveArrayCritical(members,pd,0);
		//
	}
	return res;
	return 0;
}

#ifdef __cplusplus
}
#endif
