//
//  ServerError.h
//  VocieTest
//
//  Created by mac on 14-3-11.
//  Copyright (c) 2014�� user. All rights reserved.
//

#ifndef VocieTest_ServerError_h
#define VocieTest_ServerError_h
enum  APP_ERROR{HAVE_LOGIN=100,IDLE_TIME_OUT_ERROR,NOT_LOGIN,HAVE_JOIN_ROOM,NO_JOIN_ROOM,NO_ROOM,ROOM_HAVE_EXIST,ROOM_HAVE_PLAYER,NOT_CORRECT_LOGIN_STATUS,TIME_OUT,NOT_CONNECT,HAVE_CONNECT,CONNECT_FAILED};
#include <android/log.h>
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "m1", __VA_ARGS__)
#endif
