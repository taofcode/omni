package sbs.thundercraft.co.zw.sbsfreelancer.inteface;

import android.content.Intent;

public interface MessageListener {
	public void onDialogResult(int requestCode, int resultCode, Intent data);
}
