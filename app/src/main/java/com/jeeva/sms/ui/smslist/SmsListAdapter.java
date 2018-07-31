package com.jeeva.sms.ui.smslist;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.sms.R;
import com.jeeva.sms.data.dto.Sms;
import com.jeeva.sms.utils.timeutils.TimeUtils;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SmsListAdapter extends SectioningAdapter {

    private class Section {

        String sectionLabel;

        List<Sms> smsList = new ArrayList<>();
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {

        View vRippleView;

        TextView tvSender;

        TextView tvSmsBody;

        TextView tvSmsTime;

        public ItemViewHolder(View itemView) {
            super(itemView);

            vRippleView = itemView.findViewById(R.id.sms_row_v_ripple);
            tvSender = itemView.findViewById(R.id.sms_row_tv_sender);
            tvSmsBody = itemView.findViewById(R.id.sms_row_tv_message);
            tvSmsTime = itemView.findViewById(R.id.sms_row_tv_time);
        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {

        TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }

    Sms mNewSms;

    OnBottomReachedListener mBottomReachedListener;

    HashMap<Long, Section> mDateSectionPosMap = new HashMap<>();

    List<Section> mSections = new ArrayList<>();

    public SmsListAdapter(OnBottomReachedListener bottomReachedListener, Sms newSms) {
        this.mBottomReachedListener = bottomReachedListener;
        this.mNewSms = newSms;
    }

    public void updateSmsList(List<Sms> smsList) {
        if (smsList.size() > 0) {

            for (Sms sms : smsList) {
                updateSmsData(sms, true);
            }
        }

        notifyAllSectionsDataSetChanged();
    }

    public void addNewSms(Sms newSms) {
        updateSmsData(newSms, false);
        notifyAllSectionsDataSetChanged();
    }

    private void updateSmsData(Sms sms, boolean addBottom) {
        Section currentSection;

        String content = sms.getMessageBody();

        if (!TextUtils.isEmpty(content)) {
            long dateInMs = removeTimeNoise(sms.getReceivedDate());

            if (!mDateSectionPosMap.containsKey(dateInMs)) {
                currentSection = new Section();
                currentSection.sectionLabel = convertDateMsToDate(dateInMs);

                if(addBottom) {
                    mSections.add(currentSection);
                } else {
                    mSections.add(0, currentSection);
                }
                mDateSectionPosMap.put(dateInMs, currentSection);
            } else {
                currentSection = mDateSectionPosMap.get(dateInMs);
            }

            if(addBottom) {
                currentSection.smsList.add(sms);
            } else {
                currentSection.smsList.add(0, sms);
            }
        }
    }

    private long removeTimeNoise(long dateTimeInMs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTimeInMs);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    private String convertDateMsToDate(long dateInMs) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(new Date(dateInMs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public int getNumberOfSections() {
        return mSections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return mSections.get(sectionIndex).smsList.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.inflater_sms_row, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.inflater_sms_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        View ghostView = new View(parent.getContext());
        ghostView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return new GhostHeaderViewHolder(ghostView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section section = mSections.get(sectionIndex);

        if (sectionIndex == mSections.size() - 1
                && itemIndex == section.smsList.size() - 1) {
            mBottomReachedListener.onBottomReached();
        }

        ItemViewHolder ivh = (ItemViewHolder) viewHolder;

        Sms smsData = section.smsList.get(itemIndex);
        ivh.tvSender.setText(smsData.getSenderId());
        ivh.tvSmsBody.setText(smsData.getMessageBody());
        ivh.tvSmsTime.setText(TimeUtils.calcTimeAgoAndReturnText(smsData.getReceivedDate(), System.currentTimeMillis()));

        if(null != mNewSms && mNewSms.equals(smsData)) {
            mNewSms = null;
            setFadeOutAnimation(ivh.vRippleView);
        } else {
            ivh.vRippleView.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = mSections.get(sectionIndex);

        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        hvh.titleTextView.setText(s.sectionLabel);
    }

    static void setFadeOutAnimation(View v) {
        v.setVisibility(View.VISIBLE);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 0.8f, 0f);
        fadeOut.setDuration(1500);

        AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeOut);
        mAnimationSet.start();
    }
}