package com.jeeva.sms.ui.smslist;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.sms.R;
import com.jeeva.sms.data.dto.Sms;

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

        TextView tvSmsBody;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvSmsBody = itemView.findViewById(R.id.personNameTextView);
        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }

    OnBottomReachedListener mBottomReachedListener;

    HashMap<Long, Section> mDateSectionPosMap = new HashMap<>();

    List<Section> mSections = new ArrayList<>();

    public SmsListAdapter(OnBottomReachedListener bottomReachedListener) {
        this.mBottomReachedListener = bottomReachedListener;
    }

    public void updateSmsList(List<Sms> smsList) {
        if (smsList.size() > 0) {
            long dateInMs;
            Section currentSection;
            String content;

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
        View v = inflater.inflate(R.layout.list_item_addressbook_person, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_addressbook_header, parent, false);
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

        Sms note = section.smsList.get(itemIndex);
        ivh.tvSmsBody.setText(note.getMessageBody());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = mSections.get(sectionIndex);

        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        hvh.titleTextView.setText(s.sectionLabel);
    }
}