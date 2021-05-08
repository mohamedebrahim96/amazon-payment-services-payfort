package com.payfort.fortapisimulator.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payfort.fortapisimulator.R;
import com.payfort.fortapisimulator.data.beans.PfFortReqRespParams;
import com.payfort.fortapisimulator.data.constants.Constants;

import java.util.ArrayList;
import java.util.List;


public class FortParamsAdapter extends ArrayAdapter<PfFortReqRespParams> {
    private Context mContext = null;
    private ArrayList<PfFortReqRespParams> dataSource = null;
    public ArrayList<String> paramsValues = null;
    private Constants.ENVIRONMENTS_VALUES environment = null;
    private String deviceId;

    public FortParamsAdapter(Context context, int resource, List<PfFortReqRespParams> objects, Constants.ENVIRONMENTS_VALUES environment, String deviceId) {
        super(context, resource, objects);
        this.mContext = context;
        this.dataSource = (ArrayList<PfFortReqRespParams>) objects;
        //init indexes
        paramsValues = new ArrayList<>(getCount());
        for (int i = 0; i < getCount(); i++) {
            paramsValues.set(i, "");
        }
        this.environment = environment;
        this.deviceId = deviceId;
    }

    static class ViewHolder {
        TextView keyTV;
        EditText valueET;
        MutableWatcher mWatcher;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.params_lv_row, null);
            holder = new ViewHolder();
            holder.mWatcher = new MutableWatcher();
            holder.keyTV = (TextView) convertView.findViewById(R.id.paramNameTV);
            holder.valueET = (EditText) convertView.findViewById(R.id.paramValueET);
            holder.valueET.addTextChangedListener(holder.mWatcher);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PfFortReqRespParams object = getItem(position);
        if (object != null) {

            if (holder.keyTV != null) {
                holder.keyTV.setText(object.getParamName());
            }
            holder.valueET.setEnabled(object.isActive());
            holder.valueET.setClickable(object.isActive());
            if (!object.isActive()) {
                holder.valueET.setInputType(InputType.TYPE_NULL);
            } else {
                holder.valueET.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            }
            holder.mWatcher.setActive(false);
            holder.mWatcher.setPosition(position);
            holder.mWatcher.setActive(true);


        }
        return convertView;
    }


    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MutableWatcher implements TextWatcher {

        private int mPosition;
        private boolean mActive;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                if (paramsValues.isEmpty()) {
                    for (int i = 0; i < getCount(); i++)
                        paramsValues.add(i, "");
                }
                paramsValues.set(mPosition, s.toString());
            }
        }
    }

    public void notifyDataSetChanged(LinearLayout llAsList, List<PfFortReqRespParams> fortParams, Constants.ENVIRONMENTS_VALUES environment) {
        llAsList.removeAllViews();
        this.dataSource = (ArrayList<PfFortReqRespParams>) fortParams;

        //init indexes
        paramsValues.clear();
        for (int i = 0; i < getCount(); i++) {
            paramsValues.add(i, "");
        }

        super.notifyDataSetChanged();
        this.environment = environment;
        for (int i = 0; i < getCount(); i++)
            llAsList.addView(getView(i, null, null));

        if (paramsValues.isEmpty()) {
            for (int i = 0; i < fortParams.size(); i++) {
                paramsValues.add("");
            }
        }
    }
}
