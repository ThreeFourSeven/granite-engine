package granite.core.asset;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AssetReference {

    private final AssetStore store;
    private UUID assetId;

    protected AssetReference(AssetStore store, UUID assetId) {
        this.store = store;
        this.assetId = assetId;
    }

    public AssetReference from(Asset asset) {
        store.register(asset);
        this.assetId = asset.id;
        return this;
    }

    public AssetReference save() {
        get().save();
        return this;
    }

    public AssetReference load() {
        get().load();
        return this;
    }

    public Asset get() {
        return store.assets.get(assetId);
    }

}
