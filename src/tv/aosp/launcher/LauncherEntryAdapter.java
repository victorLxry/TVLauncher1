package tv.aosp.launcher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import tv.aosp.launcher.launcherentries.AppCategoryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter used to hold launcher options
 */
public class LauncherEntryAdapter extends BaseExpandableListAdapter {
    /**
     * The context in which this adapter is operating
     */

    private Context context;

    private List<LauncherEntry> entries = new ArrayList<LauncherEntry>();

    public LauncherEntryAdapter(final Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return entries.size();
    }

    @Override
    public int getChildrenCount(int position) {
        return entries.get(position).getChildrenCount();
    }

    @Override
    public Object getGroup(int position) {
        return entries.get(position);
    }

    @Override
    public Object getChild(int group, int child) {
        return entries.get(group).getChild(child);
    }

    @Override
    public long getGroupId(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean expanded, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_entry, null);
        }

        LauncherEntryView lev = (LauncherEntryView)view.findViewById(R.id.entry);
        LauncherEntry entry = entries.get(position);
        lev.setEntry(entry);

        return view;
    }

    @Override
    public View getChildView(int group, int child, boolean expanded, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.app_category_list_entry, null);
        }

        final PackageManager pm = context.getPackageManager();

        final AppInfo appInfo = (AppInfo) getChild(group, child);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        try {
            icon.setImageDrawable(pm.getApplicationIcon(appInfo.packageName));
        } catch(PackageManager.NameNotFoundException nameNotFoundExcption) {
            Log.e(Launcher.LOG_TAG, "Problem finding icon for package "+appInfo, nameNotFoundExcption);
            icon.setImageDrawable(pm.getApplicationIcon(context.getApplicationInfo()));
        }

        TextView label = (TextView)view.findViewById(R.id.name);
        label.setText(appInfo.appName);

        return view;
    }

    @Override
    public boolean isChildSelectable(int group, int child) {
        if(entries.get(group) instanceof AppCategoryEntry) {
            return true;
        }
        return false;
    }

    /**
     * Add an LauncherEntry to the list of selectable options
     */

    public void add(final LauncherEntry entry) {
        entries.add(entry);
        Collections.sort(entries, LauncherEntryComparator.InstanceHolder.INSTANCE);
        notifyDataSetChanged();
    }
}
