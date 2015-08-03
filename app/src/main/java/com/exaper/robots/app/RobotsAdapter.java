package com.exaper.robots.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exaper.robots.R;
import com.exaper.robots.data.LoginToRobotImageConverter;
import com.exaper.robots.data.model.Robot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RobotsAdapter extends RecyclerView.Adapter<RobotsAdapter.RobotViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View container, Robot robot);
    }

    private final Context mContext;
    private final Filter filter;
    private final LoginToRobotImageConverter mLogin2ImageConverter;
    private List<Robot> mItems;
    private List<Robot> mFilteredItems;
    private CharSequence lastFilterConstraint;
    private OnItemClickListener mItemClickListener;

    public RobotsAdapter(Context context, LoginToRobotImageConverter converter) {
        mContext = context;
        mLogin2ImageConverter = converter;
        mItems = mFilteredItems = Collections.emptyList();
        filter = new RobotsFilter();
        // Having stable IDs will allow for fancy animations from RecyclerView.
        setHasStableIds(true);
    }

    public void setItems(List<Robot> items) {
        mItems = items;
        // New items will go through filtering first.
        filter.filter(lastFilterConstraint);
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void filter(CharSequence constraint) {
        filter.filter(constraint);
    }

    @Override
    public RobotViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return RobotViewHolder.create(mContext, viewGroup, mLogin2ImageConverter);
    }

    @Override
    public void onBindViewHolder(RobotViewHolder robotViewHolder, int position) {
        Robot robot = mItems.get(position);
        robotViewHolder.bind(robot);
        robotViewHolder.itemView.setOnClickListener(new RobotClickListener(robot));
    }

    @Override
    public long getItemId(int position) {
        return mFilteredItems.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mFilteredItems.size();
    }

    public static final class RobotViewHolder extends RecyclerView.ViewHolder {
        private final TextView mMerchantName;
        private final TextView mTitle;
        private final TextView mDetails;
        private final ImageView mImage;
        private final LoginToRobotImageConverter mLogin2ImageConverter;

        private RobotViewHolder(View itemView, LoginToRobotImageConverter converter) {
            super(itemView);
            mMerchantName = (TextView) itemView.findViewById(R.id.merchantName);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mDetails = (TextView) itemView.findViewById(R.id.details);
            mImage = (ImageView) itemView.findViewById(R.id.robotImage);
            mLogin2ImageConverter = converter;
        }

        public void bind(Robot robot) {
            mMerchantName.setText(itemView.getContext().getString(R.string.by_merchant, robot.getLogin()));
            mMerchantName.setTextColor(Color.BLACK);
            mTitle.setText(robot.getLogin());
            //String fullDescription = robot.getDescription();
            //String shortDescription = fullDescription.substring(0, fullDescription.indexOf('.'));
            //mDetails.setText(shortDescription);
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
            Picasso.with(itemView.getContext())
                    .load(mLogin2ImageConverter.convert(robot.getLogin()))
                    .into(mImage, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();
                            onPaletteAvailable(Palette.from(bitmap).generate());
                        }
                    });
        }

        private void onPaletteAvailable(Palette palette) {
            mMerchantName.setTextColor(palette.getLightVibrantColor(Color.GRAY));
        }

        public static RobotViewHolder create(Context context, ViewGroup parent, LoginToRobotImageConverter converter) {
            View view = LayoutInflater.from(context).inflate(R.layout.robot_list_item, parent, false);
            return new RobotViewHolder(view, converter);
        }
    }

    private class RobotClickListener implements View.OnClickListener {
        private final Robot target;

        public RobotClickListener(Robot target) {
            this.target = target;
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, target);
            }
        }
    }

    private class RobotsFilter extends Filter {
        private final RobotsSearchFilter robotsSearchFilter = new RobotsSearchFilter();

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            robotsSearchFilter.setConstraint(constraint);
            FilterResults results = new FilterResults();
            if (TextUtils.isEmpty(lastFilterConstraint)) {
                results.values = mItems;
                results.count = mItems.size();
            } else {
                List<Robot> filteredRobots = robotsSearchFilter.filter(mItems);
                results.values = filteredRobots;
                results.count = filteredRobots.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lastFilterConstraint = constraint;
            //noinspection unchecked
            mFilteredItems = (List<Robot>) results.values;
            notifyDataSetChanged();
        }
    }
}
