package com.moko.lib.loraiot.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.moko.lib.loraiot.R;
import com.moko.lib.loraiot.entity.SyncDevice;


public class SyncDeviceAdapter extends BaseQuickAdapter<SyncDevice, BaseViewHolder> {

    public SyncDeviceAdapter() {
        super(R.layout.item_sync_device);
    }

    @Override
    protected void convert(BaseViewHolder helper, SyncDevice item) {
        helper.setText(R.id.tv_name, item.macName);
        helper.setText(R.id.tv_mac, item.mac);
        helper.setImageResource(R.id.iv_select, item.isSelected ? R.drawable.ic_selected : R.drawable.ic_unselected);
    }
}
