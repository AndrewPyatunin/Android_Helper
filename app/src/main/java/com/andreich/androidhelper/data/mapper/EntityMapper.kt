package com.andreich.androidhelper.data.mapper

interface EntityMapper<I, O> {

    operator fun invoke(from: I): O
}