package com.android.spaciocrm.home.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.spaciocrm.R;
import com.android.spaciocrm.util.dialogs.ProfileBirthDateDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends DialogFragment {

    EditText txtUsername;
    EditText txtUserPassword;
    EditText txtUserFName;
    EditText txtUserLName;
    EditText txtUserEmail;
    EditText txtUserMobile;
    EditText txtUserDateBirth;

    DialogFragment dateFragment;
    Dialog d;

    public static AlertDialog passwordDialog;

    String usersData;
    String selectedGender;

    JSONArray userArr;
    JSONObject userObj;

    String profileData;

    JSONArray profileArr;
    JSONObject profileObj;

    public FragmentProfile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public void onStart() {
        super.onStart();
        d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.tbProfile);

        txtUsername = rootView.findViewById(R.id.txtUserUsername);
        txtUserPassword = rootView.findViewById(R.id.txtUserPassword);
        txtUserFName = rootView.findViewById(R.id.txtUserFirstName);
        txtUserLName = rootView.findViewById(R.id.txtUserLastName);
        txtUserEmail = rootView.findViewById(R.id.txtUserEmail);
        txtUserMobile = rootView.findViewById(R.id.txtUserMobile);
        txtUserDateBirth = rootView.findViewById(R.id.txtUserDateOfBirth);

        ImageButton btnEditUsername = rootView.findViewById(R.id.btnUNameEdit);
        ImageButton btnEditPassword = rootView.findViewById(R.id.btnPasswordEdit);
        ImageButton btnEditFirstname = rootView.findViewById(R.id.btnFNameEdit);
        ImageButton btnEditLastname = rootView.findViewById(R.id.btnLNameEdit);
        ImageButton btnEditEmail = rootView.findViewById(R.id.btnEmailEdit);
        ImageButton btnEditMobile = rootView.findViewById(R.id.btnMobileEdit);

        Spinner spnGender = rootView.findViewById(R.id.spnGender);

        final LinearLayout btnDateOfBirth = rootView.findViewById(R.id.btnBirthDate);

        Button btnUpdate = rootView.findViewById(R.id.btnUpdateProfile);

        SharedPreferences getUsersValues = getActivity().getSharedPreferences("PREFUSERS", Context.MODE_PRIVATE);
        profileData = getUsersValues.getString("usersData", "[]");

        SharedPreferences getUsersPos = getActivity().getSharedPreferences("PREFUSERSPOS", Context.MODE_PRIVATE);
        final int getPos = getUsersPos.getInt("usersPos", 9);


        try {
            profileArr = new JSONArray(profileData);

            if(getPos != 9) {

                profileObj = profileArr.getJSONObject(getPos);

                txtUsername.setText(profileObj.getString("USERNAME"));
                txtUserPassword.setText(profileObj.getString("PASSWORD"));
                txtUserFName.setText(profileObj.getString("FIRST_NAME"));
                txtUserLName.setText(profileObj.getString("LAST_NAME"));
                txtUserEmail.setText(profileObj.getString("EMAIL"));
                txtUserMobile.setText(profileObj.getString("MOBILE"));
                txtUserDateBirth.setText(profileObj.getString("DATE_OF_BIRTH"));

                if(profileObj.getString("GENDER").equals("Male")) {
                    spnGender.setSelection(1);
                }

                if(profileObj.getString("GENDER").equals("Female")) {
                    spnGender.setSelection(2);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUsername.setEnabled(true);
                txtUsername.setGravity(Gravity.START);
                txtUsername.requestFocus();
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDialog.show();
            }
        });

        btnEditFirstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUserFName.setEnabled(true);
                txtUserFName.setGravity(Gravity.START);
                txtUserFName.requestFocus();
            }
        });

        btnEditLastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUserLName.setEnabled(true);
                txtUserLName.setGravity(Gravity.START);
                txtUserLName.requestFocus();
            }
        });

        btnEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUserEmail.setEnabled(true);
                txtUserEmail.setGravity(Gravity.START);
                txtUserEmail.requestFocus();
            }
        });

        btnEditMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUserMobile.setEnabled(true);
                txtUserMobile.setGravity(Gravity.START);
                txtUserMobile.requestFocus();
            }
        });

        txtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtUsername.setGravity(Gravity.END);
                    txtUsername.setEnabled(false);
                    if(!txtUsername.getText().toString().equals("")) {
                        txtUsername.setHint(txtUsername.getText());
                    }

                }
            }
        });

        txtUserFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtUserFName.setGravity(Gravity.END);
                    txtUserFName.setEnabled(false);
                    if(!txtUserFName.getText().toString().equals("")) {
                        txtUserFName.setHint(txtUserFName.getText());
                    }

                }
            }
        });

        txtUserLName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtUserLName.setGravity(Gravity.END);
                    txtUserLName.setEnabled(false);
                    if(!txtUserLName.getText().toString().equals("")) {
                        txtUserLName.setHint(txtUserLName.getText());
                    }

                }
            }
        });

        txtUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtUserEmail.setGravity(Gravity.END);
                    txtUserEmail.setEnabled(false);
                    if(!txtUserEmail.getText().toString().equals("")) {
                        txtUserEmail.setHint(txtUserEmail.getText());
                    }

                }
            }
        });

        txtUserMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    txtUserMobile.setGravity(Gravity.END);
                    txtUserMobile.setEnabled(false);
                    if(!txtUserMobile.getText().toString().equals("")) {
                        txtUserMobile.setHint(txtUserMobile.getText());
                    }

                }
            }
        });

        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtUsername.getText().toString().equals("") || txtUserPassword.getText().toString().equals("") ||
                        txtUserFName.getText().toString().equals("") || txtUserLName.getText().toString().equals("") ||
                        txtUserEmail.getText().toString().equals("") || txtUserMobile.getText().toString().equals("") ||
                        txtUserDateBirth.getText().toString().equals("") ||selectedGender.equals("")) {

                    Snackbar.make(rootView, "All Inputs are required!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else {
                    updateProfile();
                    Dismiss();
                }

            }
        });

        btnDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFragment = new ProfileBirthDateDialogFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "Birthday Date Picker");
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        passwordDialog();
    }

    private void updateProfile() {

        SharedPreferences getUsersPos = getActivity().getSharedPreferences("PREFUSERSPOS", Context.MODE_PRIVATE);
        final int pos = getUsersPos.getInt("usersPos", 9);

        userArr = new JSONArray();

        try {

            JSONObject prObj = new JSONObject();

            prObj.put("USERNAME", txtUsername.getText().toString());
            prObj.put("PASSWORD", txtUserPassword.getText().toString());
            prObj.put("FIRST_NAME", txtUserFName.getText().toString());
            prObj.put("LAST_NAME", txtUserLName.getText().toString());
            prObj.put("EMAIL", txtUserEmail.getText().toString());
            prObj.put("MOBILE", txtUserMobile.getText().toString());
            prObj.put("DATE_OF_BIRTH", txtUserDateBirth.getText().toString());
            prObj.put("GENDER", selectedGender);
            prObj.put("POSITION", pos);

            userArr.put(pos, prObj);

            SharedPreferences prefUsersList = getActivity().getSharedPreferences("PREFUSERS", Context.MODE_PRIVATE);
            SharedPreferences.Editor listEditor = prefUsersList.edit();
            listEditor.putString("usersData", userArr.toString());
            listEditor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getActivity(), "Profile Successfully Updated!", Toast.LENGTH_LONG).show();
    }

    public void passwordDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflaters = (getActivity()).getLayoutInflater();

        View dialogView = inflaters.inflate(R.layout.update_password_layout, null);
        builder.setView(dialogView);

        final EditText txtOldPassword = dialogView.findViewById(R.id.txtOldPass);
        final EditText txtNewPassword =  dialogView.findViewById(R.id.txtNewPass);
        final EditText txtConfirmPassword =  dialogView.findViewById(R.id.txtConfirmPass);

        ImageButton btnDialogCancel = dialogView.findViewById(R.id.btnCloseUpdatePassword);
        Button btnDialogSave = dialogView.findViewById(R.id.btnUpdatePassword);

        passwordDialog = builder.create();

        SharedPreferences getUsersValues = getActivity().getSharedPreferences("PREFUSERS", Context.MODE_PRIVATE);
        usersData = getUsersValues.getString("usersData", "[]");

        SharedPreferences getUsersPos = getActivity().getSharedPreferences("PREFUSERSPOS", Context.MODE_PRIVATE);
        final int pos = getUsersPos.getInt("usersPos", 9);


        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDialog.cancel();
            }
        });

        btnDialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtOldPassword.getText().toString().equals("") || txtNewPassword.getText().toString().equals("") ||
                        txtConfirmPassword.getText().toString().equals("")) {

                   Toast.makeText(getActivity(), "All Fields Are Required", Toast.LENGTH_LONG).show();

                }

                else {

                    if(!txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                        Toast.makeText(getActivity(), "New password and confirm password do not match", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            userArr = new JSONArray(usersData);

                            if(pos != 9) {

                                userObj = userArr.getJSONObject(pos);
                                userObj.put("PASSWORD", txtNewPassword.getText().toString());

                                userArr.put(pos, userObj);

                                SharedPreferences prefUsersList = getActivity().getSharedPreferences("PREFUSERS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor listEditor = prefUsersList.edit();
                                listEditor.putString("usersData", userArr.toString());
                                listEditor.apply();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        passwordDialog.dismiss();
                    }

                }

            }
        });

    }

    public void setSelectedDate(String dateSet) {
        txtUserMobile.setGravity(Gravity.END);
        txtUserMobile.setEnabled(false);
        txtUserDateBirth.setHint(dateSet);

    }

    public void Dismiss() {
        DialogFragment dialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("ProfileFragment");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

}
