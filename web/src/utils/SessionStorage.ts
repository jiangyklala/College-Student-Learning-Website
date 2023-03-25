export class SessionStorage {

    public static get(key : any) {
        const v = sessionStorage.getItem(key);
        if (v != null) {
            return JSON.parse(v);
        }
    }

    public static set(key : any, data : any) {
        sessionStorage.setItem(key, JSON.stringify(data));
    }

    public static remove(key : any) {
        sessionStorage.removeItem(key);
    }

    public static clearAll() {
        sessionStorage.clear();
    }
}