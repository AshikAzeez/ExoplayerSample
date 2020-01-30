package com.example.exoplayer.kotlin_sample

/**
 * Created by Ashik on 14/6/19 .
 */

fun main(args: Array<String>) {

  var lambdaFunction: (String, Int) -> Unit = { s: String, n: Int ->
    println(s + n)
  }
  lambdaFunction("Sample Lambda function", 1)
  lambdaFunction = { s: String, i: Int ->
    println(s + i)
  }
  lambdaFunction("Sample Lambda function ", 2)

  val noArgueFun: () -> Unit = {
    println("No args sample")
  }

  noArgueFun()
  higherOrderSample("Higher order's Lambda", expressionSample)
  printMe {
    noArgueFun.invoke()
  }

  println(

      "lambda inside println----> ${
      returnMe {
        "returned string here"
      }
      }"
  )


  anotherlambda("sdjkfjdskbf")
}


typealias MyFirstAlias = (String) -> Unit

typealias userName = String

var anotherlambda: MyFirstAlias = {
  println(it)
}

val string: userName = "asdmsdadsnd"

fun returnMe(s: () -> String): String {
  return s()
}

fun printMe(lambda: () -> Unit) {
  lambda()
}

fun higherOrderSample(
  str: String,
  expression: (String) -> Unit
) {
  expression(str)
  println("Higher order sample")
}

val expressionSample: (s: String) -> Unit = {
  println(it)
}

fun <T> ArrayList<T>.filterCondition(condition: (T) -> Boolean): ArrayList<T> {
  val result = arrayListOf<T>()
  for (item in this) {
    if (condition(item)) {
      result.add(item)
    }
  }
  return result
}

//return an function

fun returnAnFunction(): ((Int, Int) -> Int) {
  return ::add
}

val value = returnAnFunction()
val result = value(2, 5)
fun add(
  a: Int,
  b: Int
): Int {
  return a + b
}
