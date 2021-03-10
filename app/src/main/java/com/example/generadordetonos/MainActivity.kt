package com.example.generadordetonos

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

val duration = 10 // duration of sound

val sampleRate = 22050 // Hz (maximum frequency is 7902.13Hz (B8))

val numSamples =
    duration * sampleRate//total de samples que va a dar (total de disparos al parlante)


var array = arrayListOf<Int>()


val buffer =
    ShortArray(numSamples) //VALOR DE CADA UNO DE ESOS DISPAROS EN 2 BYTE, ES DECIR 16BITS, ES DECIR -32 A +32 (2 A LA 16) si dice uno, tiene un SOLO LUGAR!

//ES DECIR, SI DURA UN SEGUNDO, A 22050 SAMPLES POR SEGUNDO, VA A DAR 22050 DISPAROS DE VALORES ENTRE -32MIL A +32MIL


var tono = 140

var samplesPorGolpes= sampleRate.toDouble()/ tono  //indica cuantos samples tiene que enviarse antes de que se produzca un golpe maximo

var s = 1

var paso = sampleRate.toDouble() / tono

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ondaSino()
//ondaCuadrada()


    }

    fun ondaSino() {


        for (x in 0 until numSamples) {
//            Log.i("|||sin((PI*x)/(50)))", "${sin((PI * x) / (50))}")
//            /**||||sin((PI*x)/(50)))||||***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/
    var sin= sin((x*PI)/(samplesPorGolpes/2)+(PI)/(2))

    buffer[x] = (Short.MAX_VALUE*sin).roundToInt().toShort()  //se da 21766.9 lo redondea para arriba, pero eso solo nomas es nada


        }

        for (x in buffer) {
            Log.i("|||x", "${x}")
            /**||||x||||***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/
        }

        var count = 0
        for (x in buffer) {
            if (x==Short.MAX_VALUE || x < (Short.MIN_VALUE+1).toShort()) { //es +1 porque el minimo es mas grande que el maximo (en temrinos absolutos, porque aprece ser que el cero queda del lado de los positivos, entonces cuando reparte el seno para formar los short del buffer carga los maximos en negatibo, que son uno menos que los minimos reales
                count++


            }

        }

        Log.i("|||count", "${count}")
        /**||||count||||***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/

        /* for (i in 0..numSamples) {
        samples[i] = Math.sin(2 * Math.PI * i / (sampleRate / note.get(0))) // GENERA VALORES ENTRE 0 Y 1

        buffer[i] = (samples[i] * Short.MAX_VALUE).toShort() // Higher amplitude increases volume //UTILIZA LOS VALORES PARA PIVOTEAR ENRE 32767 Y -32767
    }  */

        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,//PIDE PARA CREARLO EL SAMPLERATE, O LO QUE ES LO MISMO, LA CANTIDAD DE DISPAROS QUE SE REALIZARAN POR SEGUNDO
            AudioFormat.ENCODING_PCM_16BIT,
            buffer.size,//PIDE CUANTOS DISPAROS SE HARA(EL TAMAÑO DEL BUFFER ES IGUAL AL SAMPLE RATE POR LA DURACION
            /**aca si puedo alargar el buffer size(abajo no), lo alargo por 10 para que me entre el llop*/
            AudioTrack.MODE_STATIC
        )//ACA YA SABE CUANTOS DISPAROS TENDRA QUE HACER Y A QUE VELOCIDAD

        audioTrack.write(
            buffer,
            0,
            buffer.size
        );//ACA YA SABE A QUE VELOCIDAD Y CUANTOS TENDRA QUE HACER, PERO NO SABE QUE POTENCIA DE 16BITS TIENE CADA DISPARO, ENTONCES SE LA PASO CON EL BUFFER QUE ES UN ARRAY DE DISPAROS, Y NO SE PORQUE LE VUELVE A PASAR QUE TAN GRANDE ES (LO DE ARRIBA DEBE SER EL TAMAÑO MAXIMO)

        /**si juefo con el buffer del audioTrack, y lo hago mas corto, se acorta el sonido sin crashearse*/
        /**si lo alargo de crashe*/

        // audioTrack.setLoopPoints(0,40000,10) /**40000 por poner, pero es 22050*2*/
        audioTrack.play()
        /**EJECUTOOO*/


    }





























    fun ondaCuadrada() {


        for (x in 0 until numSamples) {//numSamples no lo incluye!
            if (x + 1 == ((paso * s).roundToInt())) {//VA DEL 0 AL 9, PERO TIENE QUE COMPARAR DEL 1 AL 10
                buffer[x] = (-1 * Short.MAX_VALUE).toShort()
                s++
//                Log.i("|||paso","${paso}")/**||||pasp||||***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/
            } else buffer[x] = 0
        }

        var count = 0
        for (x in buffer) {
            if (x == Short.MAX_VALUE) count++
        }

        for (x in buffer) {
            Log.i("|||buffer x", "${x}")
            /**||||x||||***************LOG**********LOG**********LOG**********LOG**********LOG**********LOG******/
        }


        /* for (i in 0..numSamples) {
        samples[i] = Math.sin(2 * Math.PI * i / (sampleRate / note.get(0))) // GENERA VALORES ENTRE 0 Y 1

        buffer[i] = (samples[i] * Short.MAX_VALUE).toShort() // Higher amplitude increases volume //UTILIZA LOS VALORES PARA PIVOTEAR ENRE 32767 Y -32767
    }  */

        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,//PIDE PARA CREARLO EL SAMPLERATE, O LO QUE ES LO MISMO, LA CANTIDAD DE DISPAROS QUE SE REALIZARAN POR SEGUNDO
            AudioFormat.ENCODING_PCM_16BIT,
            buffer.size,//PIDE CUANTOS DISPAROS SE HARA(EL TAMAÑO DEL BUFFER ES IGUAL AL SAMPLE RATE POR LA DURACION
            /**aca si puedo alargar el buffer size(abajo no), lo alargo por 10 para que me entre el llop*/
            AudioTrack.MODE_STATIC
        )//ACA YA SABE CUANTOS DISPAROS TENDRA QUE HACER Y A QUE VELOCIDAD

        audioTrack.write(
            buffer,
            0,
            buffer.size
        );//ACA YA SABE A QUE VELOCIDAD Y CUANTOS TENDRA QUE HACER, PERO NO SABE QUE POTENCIA DE 16BITS TIENE CADA DISPARO, ENTONCES SE LA PASO CON EL BUFFER QUE ES UN ARRAY DE DISPAROS, Y NO SE PORQUE LE VUELVE A PASAR QUE TAN GRANDE ES (LO DE ARRIBA DEBE SER EL TAMAÑO MAXIMO)

        /**si juefo con el buffer del audioTrack, y lo hago mas corto, se acorta el sonido sin crashearse*/
        /**si lo alargo de crashe*/

        // audioTrack.setLoopPoints(0,40000,10) /**40000 por poner, pero es 22050*2*/
        audioTrack.play()
        /**EJECUTOOO*/

    }


}