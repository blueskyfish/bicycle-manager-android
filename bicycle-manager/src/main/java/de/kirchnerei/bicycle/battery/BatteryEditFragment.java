package de.kirchnerei.bicycle.battery;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import de.kirchnerei.bicycle.BaseFragment;
import de.kirchnerei.bicycle.FloatingButtonKind;
import de.kirchnerei.bicycle.MiddlewareHandler;
import de.kirchnerei.bicycle.R;
import de.kirchnerei.bicycle.helper.Formatter;
import de.kirchnerei.bicycle.helper.Logger;
import de.kirchnerei.bicycle.helper.NumberUtils;
import de.kirchnerei.bicycle.http.HttpManager;
import kirchnerei.httpclient.HttpRequest;
import kirchnerei.httpclient.HttpResponse;
import kirchnerei.httpclient.PathBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BatteryEditFragment extends BaseFragment {

    private final BatteryEdit item = new BatteryEdit();

    private EditText mDate;
    private EditText mAverageSpeed;
    private EditText mMileage;
    private EditText mLeftover;

    private HttpManager mHttpManager;
    private Formatter mFormatter;
    private ObjectMapper mMapper;

    private int mId = 0;

    public BatteryEditFragment() {
        this.item.setDate(new Date());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mId = args.getInt(BatteryDefine.PARAM_BATTERY_ID, 0);
        }
        mHttpManager = getBicycleApplication().getHttpManager();
        mFormatter = getBicycleApplication().getFormatter();
        mMapper = getBicycleApplication().getMapper();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_battery_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDate         = (EditText) view.findViewById(R.id.battery_date);
        mAverageSpeed = (EditText) view.findViewById(R.id.battery_average_speed);
        mMileage      = (EditText) view.findViewById(R.id.battery_mileage);
        mLeftover     = (EditText) view.findViewById(R.id.battery_leftover);

        mDate.setKeyListener(null);

        View.OnClickListener datePickerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPickBatteryDate();
            }
        };

        ImageView datePicker = (ImageView) view.findViewById(R.id.battery_datepicker);
        datePicker.setOnClickListener(datePickerListener);
        mDate.setOnClickListener(datePickerListener);

        // update the current date from property "item"
        updateDate(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        MiddlewareHandler mh = getMiddlewareHandler();
        if (mId <= 0) {
            mh.changeFloatingButton(FloatingButtonKind.BATTERY_ADD, saveBatteryListener);
        } else {
            mh.changeFloatingButton(FloatingButtonKind.BATTERY_SAVE, saveBatteryListener);
        }
        if (mId > 0) {
            GetBatteryEditRequest request = new GetBatteryEditRequest();
            request.execute("battery", mId);
        }
    }

    private void updateDate(Date date) {
        if (date == null) {
            date = item.getDate();
        } else {
            item.setDate(date);
        }
        mDate.setText(mFormatter.from(date));
    }

    private void updateAverageSpeed(int averageSpeed) {
        String value = Double.toString((double) averageSpeed / 10);
        item.setAverageSpeed(averageSpeed);
        mAverageSpeed.setText(averageSpeed != 0 ? value : "");
    }

    private void updateMileage(int mileage) {
        String value = Double.toString((double) mileage / 10);
        item.setLeftover(mileage);
        mMileage.setText(mileage != 0 ? value : "");
    }

    private void updateLeftover(int leftover) {
        String value = Double.toString((double) leftover / 10);
        item.setLeftover(leftover);
        mLeftover.setText(leftover != 0 ? value : "");
    }

    private void updateItemFromEditView() {
        item.setAverageSpeed(NumberUtils.toNumber(mAverageSpeed.getText().toString()));
        item.setMileage(NumberUtils.toNumber(mMileage.getText().toString()));
        item.setLeftover(NumberUtils.toNumber(mLeftover.getText().toString()));
    }

    private void doSaveBatteryItem() {
        updateItemFromEditView();

        PostBatteryEditRequest request = new PostBatteryEditRequest();
        request.execute(mId);
    }

    private void doPickBatteryDate() {

        final Calendar now = Calendar.getInstance();
        now.setTime(item.getDate());

        DatePickerDialog dlg = new DatePickerDialog(
            getActivity(),
            // TODO Define a Theme for the DatePickerDialog
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    synchronized (now) {
                        now.set(Calendar.YEAR, year);
                        now.set(Calendar.MONTH, monthOfYear);
                        now.set(Calendar.DATE, dayOfMonth);
                        updateDate(now.getTime());
                    }
                }
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DATE)
        );
        dlg.getDatePicker().setMaxDate(System.currentTimeMillis());
        dlg.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        dlg.show();
    }

    private final View.OnClickListener saveBatteryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           doSaveBatteryItem();
        }
    };

    class GetBatteryEditRequest extends AsyncTask<Object, Void, BatteryEdit> {

        @Override
        protected BatteryEdit doInBackground(Object... params) {
            String url = PathBuilder.toUrl(params);
            Logger.debug("get battery GET %s", url);
            HttpRequest request = HttpRequest.buildGET(url);
            HttpResponse response = mHttpManager.execute(request);
            if (response.hasError()) {
                getMiddlewareHandler()
                    .makeSnackbar(R.string.battery_edit_request_error)
                    .show();
                return new BatteryEdit();
            }
            try {
                ResultBatteryEdit result = mMapper.readValue(response.getContent(),
                    ResultBatteryEdit.class);
                return result.getBattery();
            } catch (IOException e) {
                getMiddlewareHandler()
                    .makeSnackbar(R.string.battery_edit_mapper_error)
                    .show();
            }
            return new BatteryEdit();
        }

        @Override
        protected void onPostExecute(BatteryEdit item) {
            updateDate(item.getDate());
            updateAverageSpeed(item.getAverageSpeed());
            updateMileage(item.getMileage());
            updateLeftover(item.getLeftover());
        }
    }

    class PostBatteryEditRequest extends AsyncTask<Integer, Void, ResultStorage> {

        @Override
        protected ResultStorage doInBackground(Integer... params) {
            int id = params != null && params.length > 0 ? params[0] : 0;
            try {
                String sendData = mMapper.writeValueAsString(item);
                HttpRequest request;
                String url;
                if (id > 0) {
                    url = PathBuilder.toUrl("battery", id);
                    request = HttpRequest.buildPUT(url, sendData);
                } else {
                    url = PathBuilder.toUrl("battery");
                    request = HttpRequest.buildPOST(url, sendData);
                }
                HttpResponse response = mHttpManager.execute(request);
                if (response.hasError()) {
                    return new ResultStorage(-1);
                }
                return mMapper.readValue(response.getContent(), ResultStorage.class);
            } catch (IOException e) {
                // TODO Show a error message
            }
            return new ResultStorage(-1);
        }

        @Override
        protected void onPostExecute(ResultStorage result) {
            getMiddlewareHandler().onAction(R.string.fragment_battery_list,
                BaseFragment.EMPTY_BUNDLE);
        }
    }

}
