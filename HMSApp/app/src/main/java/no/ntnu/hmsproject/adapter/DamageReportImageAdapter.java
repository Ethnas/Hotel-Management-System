package no.ntnu.hmsproject.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.DamageImage;


public class DamageReportImageAdapter extends RecyclerView.Adapter<DamageReportImageAdapter.DamageReportImageViewHolder> {
    public List<DamageImage> damageImageList;
    private ArrayList<Bitmap> processedImages = new ArrayList<>();

    public DamageReportImageAdapter() {
        this.damageImageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public DamageReportImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_damrep_image, parent, false);
        return new DamageReportImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DamageReportImageViewHolder holder, int position) {
        holder.imageView.setImageBitmap(processedImages.get(position));
    }

    @Override
    public int getItemCount() {
        return damageImageList.size();
    }

    public void setDamageImageList(List<DamageImage> list) {
        this.damageImageList = list;
        convertImages();
        notifyDataSetChanged();
    }

    private void convertImages() {
        for(DamageImage damageImage : damageImageList) {
            byte[] bytes = damageImage.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            processedImages.add(bmp);
        }
    }

    class DamageReportImageViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;

        public DamageReportImageViewHolder(@NonNull View view) {
            super(view);
            this.imageView = view.findViewById(R.id.damrep_image);

        }
    }
}
