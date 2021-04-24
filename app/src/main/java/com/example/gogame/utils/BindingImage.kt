package com.example.shaadi.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.gogame.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("image")
fun setImageUrl(imageView: CircleImageView, image: Bitmap?) {
    if (image != null) {
        imageView.setImageBitmap(image)
    }
    else {
        imageView.setImageDrawable(imageView.context.resources.getDrawable(R.drawable.user))
    }
}

