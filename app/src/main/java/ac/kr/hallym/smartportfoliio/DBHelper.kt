package ac.kr.hallym.smartportfoliio

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context,"smartPortfoliio_db",null,1){
    override fun onCreate(p0: SQLiteDatabase?) {
        //학력 테이블
        val RESUME:String="create table RESUME_TB (_id integer primary key autoincrement, education)"
        p0?.execSQL(RESUME)
        //수상 테이블
        val REWARD:String="create table REWARD_TB (_id integer primary key autoincrement, reward)"
        p0?.execSQL(REWARD)
        //사용기술 테이블
        val TECH:String="create table TECH_TB (_id integer primary key autoincrement, tech)"
        p0?.execSQL(TECH)
        //경력 테이블
        val CAREER:String="create table CAREER_TB (_id integer primary key autoincrement, career)"
        p0?.execSQL(CAREER)
        // 나이, 이름, 깃, 이미지 테이블
        val BASE:String="create table BASE_TB (_id integer primary key autoincrement, name, age, git, img)"
        p0?.execSQL(BASE)
        //활동
        val DID:String="create table DID_TB (_id integer primary key autoincrement, didTitle, didAddr)"
        p0?.execSQL(DID)
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}