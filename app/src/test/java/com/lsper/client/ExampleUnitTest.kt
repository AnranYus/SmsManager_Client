package com.lsper.client

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun case(){
        val fa = Fa()
        val zi = fa as Zi
        fa.show()
        zi.show()
    }

}
open class Fa{
    open fun show(){
        println("我是父亲")
    }
}
open class Zi:Fa(){
    override fun show(){
        super.show()
        println("我是子类")
    }
}