package br.com.marvelheroes.core.exceptions

import java.lang.Exception

sealed class UIException : Exception() {

    class NoInternetException() : UIException()
}