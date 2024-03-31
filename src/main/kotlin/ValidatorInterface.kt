import errors.ValidationError

interface ValidatorInterface<T> {
    fun valid(arg: T?): ValidationError?
}
