package jp.yokomark.remoteviews.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import jp.yokomark.remoteview.reader.RemoteViewsInfo;
import jp.yokomark.remoteview.reader.RemoteViewsReader;
import jp.yokomark.remoteview.reader.action.IntentContainer;
import jp.yokomark.remoteview.reader.action.RemoteViewsAction;
import jp.yokomark.remoteview.reader.action.ViewGroupAction;

/**
 * @author KeishinYokomaku
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationWatcher extends NotificationListenerService {
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		RemoteViews views = sbn.getNotification().contentView;
		RemoteViewsInfo info = RemoteViewsReader.read(this, views);
		List<RemoteViewsAction> actions = info.getActions();
		dump(actions);
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {}

	private void dump(List<RemoteViewsAction> actions) {
		for (RemoteViewsAction action : actions) {
			Log.v("NotificationWatcher", action.getActionName());
			if (action instanceof IntentContainer) {
				IntentContainer p = (IntentContainer) action;
				Log.v("NotificationWatcher", p.getContentIntent().toString());
			} else if (action instanceof ViewGroupAction) {
				ViewGroupAction v = (ViewGroupAction) action;
				RemoteViewsInfo nested = v.getNestedViewsInfo(this);
				if (nested != null) {
					Log.v("NotificationWatcher", "nested views found====");
					dump(nested.getActions());
					Log.v("NotificationWatcher", "======================");
				}
			}
		}
	}
}
