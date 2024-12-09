package com.andreich.androidhelper.data.mapper

interface ModelMapper<I, O> {

    operator fun invoke(from: I): O
}