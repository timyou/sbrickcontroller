package com.scn.sbrickcontroller;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.scn.sbrickcontrollerprofilemanager.SBrickControllerProfile;
import com.scn.sbrickcontrollerprofilemanager.SBrickControllerProfileManagerHolder;
import com.scn.sbrickmanager.SBrick;
import com.scn.sbrickmanager.SBrickManagerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller profile list fragment.
 */
public class ControllerProfileListFragment extends Fragment {

    //
    // Private members
    //

    private static final String TAG = ControllerProfileListFragment.class.getSimpleName();

    private ControllerProfileListAdapter controllerProfileListAdapter;

    private ListView listViewControllerProfiles;
    private Button buttonAddControllerProfile;

    //
    // Constructors
    //

    public ControllerProfileListFragment() {
    }

    public static ControllerProfileListFragment newInstance() {
        ControllerProfileListFragment fragment = new ControllerProfileListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    //
    // Fragment overrides
    //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller_profile_list, container, false);

        listViewControllerProfiles = (ListView)view.findViewById(R.id.listViewControllerProfiles);
        listViewControllerProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick...");

                // Open the controller fragment
                List<SBrickControllerProfile> profiles = new ArrayList<SBrickControllerProfile>(SBrickControllerProfileManagerHolder.getManager().getProfiles());
                SBrickControllerProfile profile = profiles.get(position);
                MainActivity activity = (MainActivity)getActivity();
                activity.startControllerFragment(profile);
            }
        });
        controllerProfileListAdapter = new ControllerProfileListAdapter(getActivity());
        listViewControllerProfiles.setAdapter(controllerProfileListAdapter);

        buttonAddControllerProfile = (Button)view.findViewById(R.id.buttonAddControllerProfile);
        buttonAddControllerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick...");

                // TODO: open the controller profile editor with a blank profile
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume...");
        super.onResume();

        try {
            SBrickControllerProfileManagerHolder.getManager().loadProfiles();
        }
        catch (Exception ex) {
            Helper.showMessageBox(
                    getActivity(),
                    "Could not load controller profiles.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i(TAG, "onClick...");
                        }
            });
        }
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause...");
        super.onPause();

        try {
            SBrickControllerProfileManagerHolder.getManager().saveProfiles();
        }
        catch (Exception ex) {
            Helper.showMessageBox(
                    getActivity(),
                    "Could not save controller profiles.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i(TAG, "onClick...");
                        }
                    });
        }
    }

    //
    // Private methods and classes
    //

    private static class ControllerProfileListAdapter extends BaseAdapter {

        private Context context;

        ControllerProfileListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return SBrickControllerProfileManagerHolder.getManager().getProfiles().size();
        }

        @Override
        public Object getItem(int position) {
            return SBrickControllerProfileManagerHolder.getManager().getProfileAt(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.controller_profile_item, parent, false);
            }

            final SBrickControllerProfile profile = (SBrickControllerProfile) getItem(position);

            TextView twControllerProfileName = (TextView)rowView.findViewById(R.id.textviewControllerProfileName);
            twControllerProfileName.setText(profile.getName());

            Button btnEditProfile = (Button)rowView.findViewById(R.id.edit_controller_profile_button);
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: open the profile editor
                }
            });

            Button btnDeleteProfile = (Button)rowView.findViewById(R.id.delete_controller_profile_button);
            btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.showQuestionDialog(
                            context,
                            "Do you really want to remove this profile?",
                            "Yes",
                            "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i(TAG, "onClick...");
                                    SBrickControllerProfileManagerHolder.getManager().removeProfile(profile);
                                    ControllerProfileListAdapter.this.notifyDataSetChanged();
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing here
                                }
                            });
                }
            });
            return rowView;
        }
    }
}
